package com.danielpasser.mychat.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import com.danielpasser.mychat.models.Country
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TreeMap


class Utils {
    companion object {

        val simpleDateFormatPattern = "yyyy-MM-dd"


        private fun readJsonFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).bufferedReader().use { it.readText() }
        }

        fun getCountries(context: Application): List<Country> {
            val json = readJsonFromAssets(context = context, fileName = "country_codes.json")
            val gson = Gson()
            return gson.fromJson(json, Array<Country>::class.java).asList()

        }

        fun getCountryInfo(country: Country) =
            "${country.dialCode} ${getEmojiFlag(country.isoCode)} ${country.name}"

        fun getCountryInfoShort(country: Country) =
            "${country.dialCode} ${getEmojiFlag(country.isoCode)}"

        fun getEmojiFlag(isoString: String?): String {
            return isoString?.uppercase()
                ?.map { char -> Character.codePointAt("$char", 0) + 0x1F1A5 }
                ?.joinToString("") {
                    String(Character.toChars(it))
                } ?: ""
        }

        fun getCountryName(isoString: String): String {
            val locale = Locale("", isoString)
            return locale.displayCountry
        }

        fun searchCountry(query: String, list: List<Country>): List<Country> {
            val normalizedQuery = query.trim()
            return list.filter { country ->
                country.isoCode?.contains(normalizedQuery, true) == true || country.name?.contains(
                    normalizedQuery,
                    true
                ) == true || country.dialCode?.contains(normalizedQuery, true) == true
            }
        }

        fun String.usernameCheck(): Boolean =
            this.matches("[A-Za-z0-9_-]+".toRegex()) || this.isEmpty()

        fun imgToByteArray(uri: Uri): String {
            try {
                Log.e("TEST", uri.path.toString())
                val bm = BitmapFactory.decodeFile(uri.toString())
                val baos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
                return baos.toByteArray().toString()

            } catch (e: Exception) {
                return ""
                Log.e("TEST", e.toString())
            }
        }


//        fun bitmapToByteArray(bitmap: ImageBitmap): String? {
//            val outputStream = ByteArrayOutputStream()
//            bitmap
//                .compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//            return Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT)
//        }

        fun convertImageFileToBase64(imageFile: File): String {

            return FileInputStream(imageFile).use { inputStream ->
                ByteArrayOutputStream().use { outputStream ->
                    Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                        inputStream.copyTo(base64FilterStream)
                        base64FilterStream.close()
                        outputStream.toString()
                    }
                }
            }
        }


        fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
            // Obtain the content resolver from the context
            val contentResolver: ContentResolver = context.contentResolver

            // Check the API level to use the appropriate method for decoding the Bitmap
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // For Android P (API level 28) and higher, use ImageDecoder to decode the Bitmap
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // For versions prior to Android P, use BitmapFactory to decode the Bitmap
                val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                    Bitmap.createBitmap(BitmapFactory.decodeStream(stream))
                }
                bitmap
            }
        }


        fun Bitmap.toBase64str(): String {
            val outputStream = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }


        fun String.toBitmap(): Bitmap? {
            val base64Str = this
            return try {
                val decodedBytes = Base64.decode(
                    base64Str.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
                )
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        }

        fun convert(base64Str: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(
                    base64Str.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
                )
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        }

        fun Uri.getName(context: Context): String? {
            val returnCursor = context.contentResolver.query(this, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            if (nameIndex != null) {
                val fileName = returnCursor.getString(nameIndex)
                returnCursor.close()
                return fileName
            } else return null
        }

        fun Uri.toBitmap(context: Context): Bitmap? {
            val contentResolver: ContentResolver = context.contentResolver

            // Check the API level to use the appropriate method for decoding the Bitmap
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // For Android P (API level 28) and higher, use ImageDecoder to decode the Bitmap
                val source = ImageDecoder.createSource(contentResolver, this)
                ImageDecoder.decodeBitmap(source)
            } else {
                // For versions prior to Android P, use BitmapFactory to decode the Bitmap
                val bitmap = context.contentResolver.openInputStream(this)?.use { stream ->
                    Bitmap.createBitmap(BitmapFactory.decodeStream(stream))
                }
                bitmap
            }
        }

        fun Long?.convertMillisToDate(): String {
            val millis = this
            return if (millis == null) ""
            else {
                val formatter = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
                formatter.format(Date(millis))
            }
        }

        fun String.convertDateToMillis(): Long? {
            return try {
                val dateString = this
                val formatter = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
                val date = formatter.parse(dateString)
                date?.time
            } catch (e: Exception) {
                null
            }
        }


        fun String.fullDate(): String? {
            try {

                val dateString = this
                val formatter = DateTimeFormatter.ISO_DATE_TIME
                val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
                val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

                return zonedDateTime.format(dateTimeFormatter)
            } catch (e: Exception) {
                return null
            }
        }


        fun String.fullDateToTime(): String? {
            val dateString = this
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
            val time = zonedDateTime.toLocalTime()
            //  val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            return time.format(timeFormatter)

        }


        fun String.dateToZodiac(): String? {
            try {
                val formatter = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.time = formatter.parse(this)!!
                val month: Int = calendar.get(Calendar.MONTH) + 1
                val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
                val signs = TreeMap<Int, String>()
                signs[101] = "Capricorn"
                signs[120] = "Aquarius"
                signs[218] = "Pisces"
                signs[321] = "Aries"
                signs[420] = "Taurus"
                signs[521] = "Gemini"
                signs[621] = "Cancer"
                signs[723] = "Leo"
                signs[823] = "Pisces"
                signs[923] = "Libra"
                signs[1023] = "Scorpio"
                signs[1122] = "Sagittarius"
                signs[1222] = "Capricorn"
                val key = month*100 + day
                return signs.floorEntry(key)!!.value
            } catch (e: Exception) {
                return null
            }

        }


        fun String.convertDateFull(): Calendar? {
            return try {
                val cal = Calendar.getInstance()
                val dateString = this
                val formatter = DateTimeFormatter.ISO_DATE_TIME
                val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
                //SimpleDateFormat(simpleDateFormatPatternFull, Locale.getDefault())
                cal.time = Date.from(zonedDateTime.toInstant())
                cal
            } catch (e: Exception) {
                null
            }
        }
    }
}
