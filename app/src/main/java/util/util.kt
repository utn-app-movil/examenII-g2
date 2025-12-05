package util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import interfaces.ISebasAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val EXTRA_ID = "cr.ac.utn.appmovil.room"

class util {
    companion object{
        var apiURL = "https://rooms-api.azurewebsites.net"

        fun openActivity(context: Context
                         , objClass: Class<*>, extraName: String="", value: String?=null){
            val intent= Intent(
                context, objClass
            ).apply { putExtra(extraName, value)}
            context.startActivity(intent)
        }

        fun showDialogCondition(context: Context, titleQuestion: String, questionText: String, positiveStr: String, negativeStr: String
                                , positiveCallback: () ->  Unit, negativeCallback: () ->  Unit){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(questionText)
                .setCancelable(false)
                .setPositiveButton(positiveStr, DialogInterface.OnClickListener{
                        dialog, id -> positiveCallback()
                })
                .setNegativeButton(negativeStr, DialogInterface.OnClickListener {
                        dialog, id -> if (positiveCallback == null) dialog.cancel() else negativeCallback()
                })

            val alert = dialogBuilder.create()
            alert.setTitle(titleQuestion)
            alert.show()
        }

        /**
         * Creates and returns an instance of the API service
         */
        fun getAPIService(): ISebasAPIService {
            val retrofit = Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(ISebasAPIService::class.java)
        }
    }
}
