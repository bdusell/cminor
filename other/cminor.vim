" Vim syntax file
" Language:    C Minor
" Maintainer:  Brian DuSell <bdusell@gmail.com>
" Last Change: May 29, 2013
" Version:     1

if exists("b:current_syntax")
  finish
endif

syn keyword cmConditionalKeyword if else
syn keyword cmLoopKeyword while
syn keyword cmControlKeyword return
syn keyword cmTypeKeyword void boolean int char string
syn keyword cmCommandKeyword print
syn keyword cmBooleanKeyword true false

syn match cmIdentifier display '[A-Za-z_][A-Za-z_0-9]*'
syn match cmInteger display '[0-9]\+'

syn match cmOperator display '+\|-\|*\|!\|&&\|||\|\(=\|!\|<\|>\)=\|<\|>\|='
syn match cmStructural display '(\|)\|{\|}\|,\|;'

syn match cmSpecial display contained '\\0\|\\n\|\\"\|\\\'\|\\\\'
syn region cmString contains=cmSpecial extend start=+"+ end=+"+
syn region cmCharacter contains=cmSpecial extend start=+'+ end=+'+

syn region cmShortComment start='//' end='$'
syn region cmLongComment extend start='/\*' end='\*/'

highlight link cmConditionalKeyword Conditional
highlight link cmLoopKeyword Repeat
highlight link cmControlKeyword Statement
highlight link cmTypeKeyword Type
highlight link cmCommandKeyword Statement
highlight link cmBooleanKeyword Boolean

highlight link cmIdentifier Identifier
highlight link cmInteger Number

highlight link cmSpecial SpecialChar
highlight link cmString String
highlight link cmCharacter Character

highlight link cmShortComment Comment
highlight link cmLongComment Comment

" Custom cminor colors
hi cmConditionalKeyword ctermfg=yellow
hi cmLoopKeyword ctermfg=yellow
hi cmControlKeyword ctermfg=yellow
hi cmTypeKeyword ctermfg=green
hi cmCommandKeyword ctermfg=yellow
hi cmBooleanKeyword ctermfg=red
hi cmIdentifier ctermfg=gray
hi cmInteger ctermfg=red
hi cmOperator ctermfg=gray
hi cmStructural ctermfg=gray
hi cmSpecial ctermfg=magenta
hi cmString ctermfg=red
hi cmCharacter ctermfg=red
hi cmShortComment ctermfg=blue
hi cmLongComment ctermfg=blue

let b:current_syntax = "cminor"

