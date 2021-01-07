// package dotty.dokka.hljs

// import scala.util.matching.Regex
// import scala.scalajs.js.UndefOr

// case class Keywords(
//   $pattern: Regex,
//   keyword: String,
//   literal: UndefOr[String] = js.undefined,
//   built_in: UndefOr[String] = js.undefined
// )

// case class ClassName(
//   className: String,
//   begin: UndefOr[Regex] = js.undefined,
//   relevance: UndefOr[Int],
//   begin: UndefOr[Regex] = js.undefined,
//   end: UndefOr[Regex] = js.undefined,
//   keywords: UndefOr[Keywords] = js.undefined,
//   constains: UndefOr[Seq[ClassName]] = js.undefined,
//   excludeBegin: UndefOr[Boolean] = js.undefined,
//   excludeEnd: UndefOr[Boolean] = js.undefined
// )

// case class


// @js.native
// trait HLJS extends js.Object:
//   val C_BLOCK_COMMENT_MODE: ClassName = js.native
//   val QUOTE_STRING_MODE: ClassName = js.native

// def highlightDotty(hljs) =

//   // identifiers
//   val camelCaseId = raw"/[a-z][$\w]*/".r
//   val capitalizedId = raw"/\b[A-Z][$\w]*\b/".r
//   val alphaId = raw"/[a-zA-Z$_][$\w]*/".r
//   val op = raw"/[^\s\w\d,\"'()[\]{}]+/".r
//   val id = raw"(${alphaId.source}((?<=_)${op.source})?|${op.source}|\`.*?\`)".r

//   // numbers
//   val hexDigit = raw"'[a-fA-F0-9]'".r
//   val hexNumber = raw"0[xX]${hexDigit}((${hexDigit}|_)*${hexDigit}+)?".r
//   val decNumber = raw"0|([1-9]((\\d|_)*\\d)?)".r
//   val exponent = raw"[eE][+-]?\\d((\\d|_)*\\d)?".r
//   val floatingPointA = raw"(${decNumber})?\\.\\d((\\d|_)*\\d)?${exponent}[fFdD]?".r
//   val floatingPointB = raw"${decNumber}${exponent}[fFdD]?".r
//   val number = raw"(${hexNumber}|${floatingPointA}|${floatingPointB}|(${decNumber}[lLfFdD]?))".r

//   // Regular Keywords
//   // The "soft" keywords (e.g. 'using') are added later where necessary

//   val alwaysKeywords = Keywords(
//     raw"/(\w+|\?=>|\?{1,3}|=>>|=>|<:|>:|_|<-|\.nn)/".r,
//     "abstract case catch class def do else enum export extends final finally for given " +
//       "if implicit import lazy match new object package private protected override return " +
//       "sealed then throw trait true try type val var while with yield =>> => ?=> <: >: _ ? <-",
//     "true false null this super",
//     "??? asInstanceOf isInstanceOf assert implicitly locally summon .nn"
//   )
//   val modifiers = raw"abstract|final|implicit|override|private|protected|sealed".r

//   // End of class, enum, etc. header
//   val templateDeclEnd = raw"/(\/[/*]|{|: *\n|\n(?! *(extends|with|derives)))/".r

//   // name <title>
//   def titleFor(name: String) = ClassName("title", raw"(?<=${name} )${id.source}".r)

//   // all the keywords + soft keywords, separated by spaces
//   def withSoftKeywords(kwd: String) = Keywords(
//     alwaysKeywords.$pattern,
//     s"${kwd} ${alwaysKeywords.keyword}",
//     alwaysKeywords.literal,
//     alwaysKeywords.built_in
//   )

//   val PROBABLY_TYPE = ClassName("type", capitalizedId, 0)
//   val NUMBER = ClassName("number", number, 0)

//   val TPARAMS = ClassName(
//     "tparams",
//     begin = raw"/\[/".r,
//     end = raw"/\]/".r,
//     keywords = Keywords(
//       raw"/<:|>:|[+-?_:]/".r,
//       "<: >: : + - ? _"
//     ),
//     contains = Seq(
//       hljs.C_BLOCK_COMMENT_MODE,
//       ClassName("type", alphaId),
//     ),
//     relevance = 3
//   )

//   // Class or method parameters declaration
//   val PARAMS = ClassName(
//     "params",
//     begin = raw"/\(/".r,
//     end = raw"/\)/".r
//     excludeBegin = true,
//     excludeEnd = true,
//     keywords = withSoftKeywords("inline using"),
//     contains = Seq(
//       hljs.C_BLOCK_COMMENT_MODE,
//       hljs.QUOTE_STRING_MODE,
//       NUMBER,
//       PROBABLY_TYPE
//     )
//   )

//   // (using T1, T2, T3)
//   val CTX_PARAMS = ClassName(
//     "params",
//     begin = raw"/\(using (?!\w+:)/".r,
//     end = raw"/\)/".r,
//     excludeBegin = true,
//     excludeEnd = true,
//     relevance = 5,
//     keywords = withSoftKeywords("using"),
//     contains = Seq(PROBABLY_TYPE)
//   )


//   // String interpolation
//   val SUBST = ClassName(
//     "subst",
//     variants:
//   )


//   {
//     className: 'subst',
//     variants: [
//       {begin: /\$[a-zA-Z_]\w*/},
//       {
//         begin: /\${/, end: /}/,
//         contains: [
//           NUMBER,
//           hljs.QUOTE_STRING_MODE
//         ]
//       }
//     ]
//   }

//   val STRING = {
//     className: 'string',
//     variants: [
//       hljs.QUOTE_STRING_MODE,
//       {
//         begin: '"""', end: '"""',
//         contains: [hljs.BACKSLASH_ESCAPE],
//         relevance: 10
//       },
//       {
//         begin: alphaId.source + '"', end: '"',
//         contains: [hljs.BACKSLASH_ESCAPE, SUBST],
//         illegal: /\n/,
//         relevance: 5
//       },
//       {
//         begin: alphaId.source + '"""', end: '"""',
//         contains: [hljs.BACKSLASH_ESCAPE, SUBST],
//         relevance: 10
//       }
//     ]
//   }

//   // Class or method apply
//   val APPLY = {
//     begin: /\(/, end: /\)/,
//     excludeBegin: true, excludeEnd: true,
//     keywords: {
//       $pattern: alwaysKeywords.$pattern,
//       keyword: 'using ' + alwaysKeywords.keyword,
//       literal: alwaysKeywords.literal,
//       built_in: alwaysKeywords.built_in
//     },
//     contains: [
//       STRING,
//       NUMBER,
//       hljs.C_BLOCK_COMMENT_MODE,
//       PROBABLY_TYPE,
//     ]
//   }

//   // @annot(...) or @my.package.annot(...)
//   val ANNOTATION = {
//     className: 'meta',
//     begin: `@${id.source}(\\.${id.source})*`,
//     contains: [
//       APPLY,
//       hljs.C_BLOCK_COMMENT_MODE
//     ]
//   }

//   // Documentation
//   val SCALADOC = hljs.COMMENT('/\\*\\*', '\\*/', {
//     contains: [
//       {
//         className: 'doctag',
//         begin: /@[a-zA-Z]+/
//       },
//       // markdown syntax elements:
//       {
//         className: 'code',
//         variants: [
//           {begin: /```.*\n/, end: /```/},
//           {begin: /`/, end: /`/}
//         ],
//       },
//       {
//         className: 'bold',
//         variants: [
//           {begin: /\*\*/, end: /\*\*/},
//           {begin: /__/, end: /__/}
//         ],
//       },
//       {
//         className: 'emphasis',
//         variants: [
//           {begin: /\*(?![\*\s/])/, end: /\*/},
//           {begin: /_/, end: /_/}
//         ],
//       },
//       {
//         className: 'bullet', // list item
//         begin: /- (?=\S)/, end: /\s/,
//       },
//       {
//         className: 'link',
//         begin: /(?<=\[.*?\])\(/, end: /\)/,
//       }
//     ]
//   })

//   // Methods
//   val METHOD = {
//     className: 'function',
//     begin: `((${modifiers}|transparent|inline) +)*def`, end: / =|\n/,
//     excludeEnd: true,
//     relevance: 5,
//     keywords: withSoftKeywords('inline transparent'),
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       titleFor('def'),
//       TPARAMS,
//       CTX_PARAMS,
//       PARAMS,
//       PROBABLY_TYPE
//     ]
//   }

//   // Variables & constants
//   val VAL = {
//     beginKeywords: 'val var', end: /[=:;\n]/,
//     excludeEnd: true,
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       titleFor('(val|var)')
//     ]
//   }

//   // Type declarations
//   val TYPEDEF = {
//     className: 'typedef',
//     begin: `((${modifiers}|opaque) +)*type`, end: /[=;\n]/,
//     excludeEnd: true,
//     keywords: withSoftKeywords('opaque'),
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       titleFor('type'),
//       PROBABLY_TYPE
//     ]
//   }

//   // Given instances (for the soft keyword 'as')
//   val GIVEN = {
//     begin: /given/, end: /[=;\n]/,
//     excludeEnd: true,
//     keywords: 'as given using',
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       titleFor('given'),
//       PARAMS,
//       PROBABLY_TYPE
//     ]
//   }

//   // Extension methods
//   val EXTENSION = {
//     begin: /extension/, end: /(\n|def)/,
//     returnEnd: true,
//     keywords: 'extension implicit using',
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       CTX_PARAMS,
//       PARAMS,
//       PROBABLY_TYPE
//     ]
//   }

//   // 'end' soft keyword
//   val END = {
//     begin: `end(?= (if|while|for|match|try|given|extension|this|val|${id.source})\\n)`, end: /\s/,
//     keywords: 'end'
//   }

//   // Classes, traits, enums, etc.
//   val EXTENDS_PARENT = {
//     begin: ' extends ', end: /( with | derives |\/[/*])/,
//     endsWithParent: true,
//     returnEnd: true,
//     keywords: 'extends',
//     contains: [APPLY, PROBABLY_TYPE]
//   }
//   val WITH_MIXIN = {
//     begin: ' with ', end: / derives |\/[/*]/,
//     endsWithParent: true,
//     returnEnd: true,
//     keywords: 'with',
//     contains: [APPLY, PROBABLY_TYPE],
//     relevance: 10
//   }
//   val DERIVES_TYPECLASS = {
//     begin: ' derives ', end: /\n|\/[/*]/,
//     endsWithParent: true,
//     returnEnd: true,
//     keywords: 'derives',
//     contains: [PROBABLY_TYPE],
//     relevance: 10
//   }

//   val CLASS = {
//     className: 'class',
//     begin: `((${modifiers}|open|case) +)*class|trait|enum|object|package object`, end: templateDeclEnd,
//     keywords: withSoftKeywords('open'),
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       titleFor('(class|trait|object|enum)'),
//       TPARAMS,
//       CTX_PARAMS,
//       PARAMS,
//       EXTENDS_PARENT,
//       WITH_MIXIN,
//       DERIVES_TYPECLASS,
//       PROBABLY_TYPE
//     ]
//   }

//   // Case in enum
//   val ENUM_CASE = {
//     begin: /case (?!.*=>)/, end: /\n/,
//     keywords: 'case',
//     excludeEnd: true,
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       {
//         // case A, B, C
//         className: 'title',
//         begin: `(?<=(case|,) *)${id.source}`
//       },
//       PARAMS,
//       EXTENDS_PARENT,
//       WITH_MIXIN,
//       DERIVES_TYPECLASS,
//       PROBABLY_TYPE
//     ]
//   }

//   // Case in pattern matching
//   val MATCH_CASE = {
//     begin: /case/, end: /=>/,
//     keywords: 'case',
//     excludeEnd: true,
//     contains: [
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       {
//         begin: /[@_]/,
//         keywords: {
//           $pattern: /[@_]/,
//           keyword: '@ _'
//         }
//       },
//       NUMBER,
//       STRING,
//       PROBABLY_TYPE
//     ]
//   }

//   // inline someVar[andMaybeTypeParams] match
//   val INLINE_MATCH = {
//     begin: /inline [^\n:]+ match/,
//     keywords: 'inline match'
//   }

//   return {
//     name: 'Scala3',
//     aliases: ['scala', 'dotty'],
//     keywords: alwaysKeywords,
//     contains: [
//       NUMBER,
//       STRING,
//       SCALADOC,
//       hljs.C_LINE_COMMENT_MODE,
//       hljs.C_BLOCK_COMMENT_MODE,
//       METHOD,
//       VAL,
//       TYPEDEF,
//       CLASS,
//       GIVEN,
//       EXTENSION,
//       ANNOTATION,
//       ENUM_CASE,
//       MATCH_CASE,
//       INLINE_MATCH,
//       END,
//       APPLY,
//       PROBABLY_TYPE
//     ]
//   }
// }

