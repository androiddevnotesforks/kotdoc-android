package no.mhl.kotdoc.data.local

import no.mhl.kotdoc.data.local.Pattern.*

class MarkdownParser(
    private val lines: List<String>
) {

    // region Root Document
    private val document: Document = Document()
    // endregion

    // region Properties
    private var currentIndex: Int = 0
    // endregion

    // region Document Retrieval
    fun parseAsDocument(): Document {
        beginBlockParse()
        return document
    }
    // endregion

    // region Block Parsing
    private fun beginBlockParse() {
        if (currentIndex != lines.lastIndex) {
            when {
                matchFor(FencedCode) -> parseFencedCode()
                matchFor(Alert) -> parseAlert()
                matchFor(Heading) -> parseHeading()
                blankLine() -> parseNewLine()
                else -> parseParagraph()
            }
        }
    }
    // endregion

    // region Paragraph
    private fun parseParagraph() {
        println("Parsing: [Paragraph]")

        val paragraph = Paragraph("")

        while (isComplexBlock().not() && currentIndex != lines.lastIndex) {
            paragraph.content += lines[currentIndex]
            currentIndex++
        }

        document.append(paragraph)
        beginBlockParse()
    }
    // endregion

    // region Heading
    private fun parseHeading() {
        println("Parsing: [Heading]")

        val level = lines[currentIndex].count { it == '#' }
        val line = lines[currentIndex].replace("#", "").trim()

        document.append(Heading(line, level))
        currentIndex++
        beginBlockParse()
    }
    // endregion

    // region New Line
    private fun parseNewLine() {
        println("Parsing: [NewLine]")

        document.append(NewLine(""))
        currentIndex++
        beginBlockParse()
    }
    // endregion

    // region Fenced Code
    private fun parseFencedCode() {
        println("Parsing: [FencedCode]")

        val code = FencedCode("")
        var codeBlockMatches = 0

        while (codeBlockMatches < 2) {
            if (matchFor(FencedCode))  {
                codeBlockMatches++
            } else {
                val potentialNewLine = if (code.content == "") "" else "\n"
                code.content += "$potentialNewLine${lines[currentIndex]}"
            }
            currentIndex++
        }

        document.append(code)
        beginBlockParse()
    }
    // endregion

    //region Alert
    private fun parseAlert() {
        println("Parsing: [Alert]")

        val alert = Alert("")
        var fullMatch = false

        while(fullMatch.not()) {
            fullMatch = matchFor(AlertType)

            if (fullMatch.not()) {
                val line = lines[currentIndex].replace("> ", "")
                alert.content += line
            }

            currentIndex++
        }

        document.append(alert)
        beginBlockParse()
    }
    // endregion

    // region Helper Methods
    private fun isComplexBlock(): Boolean {
        return when {
            matchFor(FencedCode) -> true
            matchFor(Alert) -> true
            matchFor(Heading) -> true
            blankLine() -> true
            else -> false
        }
    }

    private fun matchFor(pattern: Pattern): Boolean {
        return lines[currentIndex].matches(Regex(pattern.literal))
    }

    private fun blankLine(): Boolean {
        return lines[currentIndex].isBlank()
    }
    // endregion

}