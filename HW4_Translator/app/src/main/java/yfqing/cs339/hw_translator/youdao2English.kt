package yfqing.cs339.hw_translator

import com.google.gson.annotations.SerializedName
class youdao2English {
    @SerializedName("web_trans") val web_trans : Web_trans?=null

    class Web_trans{
        @SerializedName("web-translation") val web_translation : List<Web_translation>? = null
    }
    class  Web_translation{
        //        @SerializedName("@same") val @same : Boolean,
//        @SerializedName("key") val key : String,
//        @SerializedName("key-speech") val key-speech-speech : String,
        @SerializedName("trans") val trans : List<Trans>?=null
    }
    class Trans{
        @SerializedName("value") val value : String?=null
    }
}