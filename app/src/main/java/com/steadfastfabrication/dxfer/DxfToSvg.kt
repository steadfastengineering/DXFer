package com.steadfastfabrication.dxfer

import org.kabeja.dxf.DXFDocument
import org.kabeja.parser.Parser
import org.kabeja.parser.ParserBuilder
import org.kabeja.svg.SVGGenerator
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.collections.mutableMapOf

fun convertDxfToSvg(dxfFile: File): String? {
    try {
        // Step 1: Parse DXF file
        val parser: Parser = ParserBuilder.createDefaultParser()
        FileInputStream(dxfFile).use { inputStream ->
            parser.parse(inputStream, "UTF-8")
        }
        val doc: DXFDocument = parser.getDocument()

        // Step 2: Generate SVG string
        val svgOutput = StringWriter()
        val handler = object : DefaultHandler() {
            private val writer = PrintWriter(svgOutput)

            override fun startDocument() {
                writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                writer.println("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">")
            }

            override fun endDocument() {
                writer.println("</svg>")
                writer.flush()
            }

            override fun characters(ch: CharArray, start: Int, length: Int) {
                writer.write(ch, start, length) // Fixed: Use write instead of print
            }

            override fun startElement(uri: String, localName: String, qName: String, attributes: org.xml.sax.Attributes) {
                writer.print("<$qName")
                for (i in 0 until attributes.length) {
                    writer.print(" ${attributes.getQName(i)}=\"${attributes.getValue(i)}\"")
                }
                writer.print(">")
            }

            override fun endElement(uri: String, localName: String, qName: String) {
                writer.print("</$qName>")
            }
        }

        val generator = SVGGenerator()
        generator.setProperties(mutableMapOf<String, Any>()) // Empty map for props
        generator.generate(doc, handler, mutableMapOf<String, Any>()) // Empty map for generation
        return svgOutput.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        android.util.Log.e("DXF", "Failed to convert DXF to SVG: ${e.message}")
        return null
    }
}