package cminor.ast;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;
import cminor.parser.LocationInfo;
import cminor.parser.DotNode;
import cminor.symbol.Symbol;
import cminor.semantic.ErrorLogger;
import cminor.visit.Visitor;
import cminor.visit.DotLabelVisitor;
import cminor.visit.ChildVisitor;
import cminor.visit.SymbolDotLabelVisitor;
import cminor.visit.GetSymbolVisitor;
import cminor.visit.SymbolResolutionVisitor;
import cminor.visit.TypeCheckVisitor;
import cminor.visit.CodeGenerationVisitor;

/* Abstract base class for abstract syntax tree nodes. */
public abstract class AstNode implements DotNode {

	// Allows each node to be given a unique id which can be used for generating
	// dot files.
	private static int numInstances = 0;

	// A unique ID number to be used when printing dot graphs.
	private int instanceNumber;

	// Information representing where in a source file the code for this node was
	// found.
	private LocationInfo location;

	protected AstNode(LocationInfo location) {
		this.instanceNumber = numInstances++;
		this.location = location;
	}

	public LocationInfo getLocation() {
		return this.location;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public String getDotId() {
		return "AstNode" + this.instanceNumber;
	}

	public String getDotLabel() {
		DotLabelVisitor v = new DotLabelVisitor();
		accept(v);
		return v.getLabel();
	}

	/* Print a dot code representation of the abstract syntax tree. */
	public final void printDotCode(PrintStream output) {

		DotLabelVisitor labelVisitor = new DotLabelVisitor();
		ChildVisitor childVisitor = new ChildVisitor();
		SymbolDotLabelVisitor symbolLabelVisitor = new SymbolDotLabelVisitor();
		GetSymbolVisitor symbolVisitor = new GetSymbolVisitor();

		List<AstNode> nodes = new ArrayList<AstNode>();
		List<Edge> edges = new ArrayList<Edge>();
		List<Symbol> symbols = new ArrayList<Symbol>();
		List<Edge> symbolEdges = new ArrayList<Edge>();

		getNodesAndEdges(childVisitor, symbolVisitor, nodes, edges, symbols, symbolEdges);

		output.println("digraph {");
		output.println("\tgraph [ordering=\"out\"];");

		for(AstNode n : nodes) {
			n.accept(labelVisitor);
			output.println("\t" + n.getDotId() + " [label=\"" + labelVisitor.getLabel() + "\"];");
		}

		for(Edge edge : edges) {
			output.println("\t" + edge.source.getDotId() + " -> " + edge.dest.getDotId() + ";");
		}

		for(Symbol s : symbols) {
			s.accept(symbolLabelVisitor);
			output.println("\t" + s.getDotId() + " [label=\"" + symbolLabelVisitor.getLabel() + "\", shape=box]");
		}

		for(Edge edge : symbolEdges) {
			output.println("\t" + edge.source.getDotId() + " -> " + edge.dest.getDotId() + " [style=\"dashed\"];");
		}

		output.println("}");

	}

	public final void resolveSymbols(ErrorLogger logger) {
		accept(new SymbolResolutionVisitor(logger));
	}

	public final void typeCheck(ErrorLogger logger) {
		accept(new TypeCheckVisitor(logger));
	}

	public final void generateCode(PrintStream output) {
		accept(new CodeGenerationVisitor(output));
	}

	private static final class Edge {

		public DotNode source;
		public DotNode dest;

		public Edge(DotNode source, DotNode dest) {
			this.source = source;
			this.dest = dest;
		}

	}

	private void getNodesAndEdges(
		ChildVisitor childVisitor, GetSymbolVisitor symbolVisitor,
		List<AstNode> nodes, List<Edge> edges, List<Symbol> symbols, List<Edge> symbolEdges)
	{

		nodes.add(this);
		List<AstNode> children = new ArrayList<AstNode>();
		childVisitor.setChildren(children);
		accept(childVisitor);
		for(AstNode c : children) {
			edges.add(new Edge(this, c));
			c.getNodesAndEdges(childVisitor, symbolVisitor,
				nodes, edges, symbols, symbolEdges);
		}

		List<Symbol> childSymbols = new ArrayList<Symbol>();
		symbolVisitor.setSymbols(childSymbols);
		accept(symbolVisitor);
		for(Symbol s : childSymbols) {
			if(s != null) {
				symbols.add(s);
				symbolEdges.add(new Edge(this, s));
			}
		}
	}

}
