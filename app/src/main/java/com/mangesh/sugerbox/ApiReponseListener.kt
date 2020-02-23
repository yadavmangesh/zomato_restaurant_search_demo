package com.mangesh.sugerbox

import com.android.volley.VolleyError

interface ApiReponseListener {
    fun onSuccess(response:String)
    fun onError(volleyError: VolleyError)
}