package data.local

import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile

actual fun readFileContent(filePath: String): String {
    return NSString.stringWithContentsOfFile(filePath) ?: throw IllegalArgumentException("Unable to read file: $filePath")
}