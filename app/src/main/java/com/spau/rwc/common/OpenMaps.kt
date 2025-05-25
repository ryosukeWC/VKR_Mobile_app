package com.spau.rwc.common
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.content.Context

class OpenMaps(private val context: Context) {

    private fun openInGoogleMaps(address: String) {
        try {
            val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Если Google Maps не установлен, открываем в веб-браузере
            val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun openInYandexMaps(address: String) {
        try {
            val uri = Uri.parse("yandexmaps://maps.yandex.ru/?text=${Uri.encode(address)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Если Яндекс.Карты не установлены, открываем в веб-браузере
            val uri = Uri.parse("https://yandex.ru/maps/?text=${Uri.encode(address)}")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun openIn2Gis(address: String) {
        try {
            val uri = Uri.parse("dgis://2gis.ru/routeSearch/rsType/car/to/${Uri.encode(address)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Если 2GIS не установлен, открываем в веб-браузере
            val uri = Uri.parse("https://2gis.ru/search/${Uri.encode(address)}")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    fun showMapChooserDialog(address: String) {
        val maps = arrayOf("Google Maps", "Яндекс.Карты", "2GIS")

        AlertDialog.Builder(context)
            .setTitle("Открыть в...")
            .setItems(maps) { _, which ->
                when (which) {
                    0 -> openInGoogleMaps(address)
                    1 -> openInYandexMaps(address)
                    2 -> openIn2Gis(address)
                }
            }
            .show()
    }
}