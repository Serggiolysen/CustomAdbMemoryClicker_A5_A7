package one.hix.testadbshell

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.core.content.FileProvider
import org.apache.commons.io.IOUtils
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

class AdbService : Service() {
    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun adbShellPing(cmd: String): String {
        val process = Runtime.getRuntime().exec(arrayOf("su"))
        val stdout = DataOutputStream(process.outputStream)
        stdout.writeBytes(cmd)
        stdout.flush()
        stdout.close()
        var theString = IOUtils.readLines(process.inputStream, "UTF_8")
            .toString()
        return theString
    }

    private fun startFaceappWithIntent(int: Int) {
        val imageFile = FileProvider.getUriForFile(
            applicationContext,
            "one.hix.testadbshell.fileprovider", File(
                Environment.getExternalStorageDirectory(),
                "final_dataset/${int}.jpg"
            )
        )
        println("sss ${imageFile.encodedPath}")
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, imageFile)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread(Runnable {
            run {
                TimeUnit.MILLISECONDS.sleep(100)
                val beginPoint = 3149
                val endPoint = 3946
                startInput(beginPoint, endPoint)
            }
        }).start()
        return START_STICKY
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun adbShellInput(cmd: String): Int {
        val process = Runtime.getRuntime().exec(arrayOf("su"))
        val stdout = DataOutputStream(process.outputStream)
        stdout.writeBytes(cmd)
        stdout.flush()
        stdout.close()
        var theString = IOUtils.readLines(process.inputStream, "UTF_8")
            .toString()
            .replace(" ".toRegex(), "")
            .replace(" ".toRegex(), "")
            .replace(" ".toRegex(), "")
            .replace(".+,Views:".toRegex(), "")
            .replace("ViewR.+".toRegex(), "")
            .trim('[')
            .trim(']')

        if (theString.length > 3) theString = "-1"
        if (theString.isEmpty()) theString = "-2"
        return theString.toInt()
    }

    private fun startInput(beginPoint: Int, endPoint: Int) {
        for (i in beginPoint..endPoint) {
            adbShellInput("am force-stop io.faceapp")

            if (!isNetworkConnected()){
                adbShellInput("svc wifi disable")
                TimeUnit.MILLISECONDS.sleep(4000)
                println("sss isNetworkConnected? ${isNetworkConnected()}")
                adbShellInput("svc wifi enable")
                TimeUnit.MILLISECONDS.sleep(13000)
                println("sss isNetworkConnected? ${isNetworkConnected()}")
            }

            startFaceappWithIntent(i)

            //нажатие на "макияж"
//            for (j in 1..200) {
//                Thread.sleep(200)
//                if (adbShellInput("dumpsys meminfo io.faceapp") > 95) {
//                    println("sss нажатие на макияж (95 - 170) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
//                    adbShellInput("input tap 480 1600")
//                    break
//                } else {
//                    if (j == 200) {
//                        adbShellInput("am force-stop io.faceapp")
//                        TimeUnit.MILLISECONDS.sleep(400)
//                        adbShellInput("input keyevent KEYCODE_BACK")
//                        TimeUnit.MILLISECONDS.sleep(400)
//                        startInput(i + 1, endPoint)
//                    }
//                }
//            }


//            TimeUnit.MILLISECONDS.sleep(2200)
//            //свайп влево
//            println("sss свайп влево")
//            adbShellInput("input swipe 900 1600 22 1600 1000")
//            TimeUnit.MILLISECONDS.sleep(500)

//            нажатие на фильтр "голливуд2"
            for (j in 1..200) {
                Thread.sleep(200)
                if (adbShellInput("dumpsys meminfo io.faceapp") > 170) {
                    println("sss нажатие на фильтр (170 - 220) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                    adbShellInput("input tap 320 1680")
                    break
                } else {
                    if (j == 200) {
                        adbShellInput("am force-stop io.faceapp")
                        TimeUnit.MILLISECONDS.sleep(400)
                        adbShellInput("input keyevent KEYCODE_BACK")
                        TimeUnit.MILLISECONDS.sleep(400)
                        startInput(i + 1, endPoint)
                    }
                }
            }

            //нажатие на фильтр " темный матовый"
            for (j in 1..200) {
                Thread.sleep(200)
                println("sss нажатие на фильтр темный матовый (210 - 245) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                if (adbShellInput("dumpsys meminfo io.faceapp") > 210) {
//                    println("sss нажатие на фильтр темный матовый (170 - 220) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                    println("sss нажатие на фильтр темный матовый (210 - 245) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                    adbShellInput("input tap 800 1600")
                    break
                } else if(adbShellInput("dumpsys meminfo io.faceapp") < 195){
                    adbShellInput("am force-stop io.faceapp")
                    TimeUnit.MILLISECONDS.sleep(400)
                    adbShellInput("input keyevent KEYCODE_BACK")
                    TimeUnit.MILLISECONDS.sleep(400)
                    startInput(i + 1, endPoint)
                }
                else {
                    if (j == 200) {
                        adbShellInput("am force-stop io.faceapp")
                        TimeUnit.MILLISECONDS.sleep(400)
                        adbShellInput("input keyevent KEYCODE_BACK")
                        TimeUnit.MILLISECONDS.sleep(400)
                        startInput(i + 1, endPoint)
                    }
                }
            }
//


//            TimeUnit.MILLISECONDS.sleep(1100)
//            //нажатие на тип фильтра "темный матовый"
//            println("sss нажатие на тип фильтра темный матовый")
//            adbShellInput("input tap 800 1600")
//            TimeUnit.MILLISECONDS.sleep(4000)

//            TimeUnit.MILLISECONDS.sleep(2200)
//            //нажатие на  уровень фильтра "3"
//            println("sss нажатие на уровень фильтра 4 (> 220) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
//            adbShellInput("input tap 660 1460")
//            TimeUnit.MILLISECONDS.sleep(4000)

//            //нажатие на "применить"
//            println("sss нажатие на применить  ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
//            adbShellInput("input tap 930 1860")
//            Thread.sleep(1000)

            //нажатие на "сохранить"
            for (j in 1..100) {
                println("sss нажатие на сохранить (105 - 125) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                Thread.sleep(200)
                println("sss нажатие на сохранить (105 - 125) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                if (adbShellInput("dumpsys meminfo io.faceapp") >245) {
                    println("sss нажатие на сохранить (105 - 125) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
                    adbShellInput("input tap 930 110")
                    break
                } else {
                    if (j == 100) {
                        adbShellInput("am force-stop io.faceapp")
                        TimeUnit.MILLISECONDS.sleep(400)
                        adbShellInput("input keyevent KEYCODE_BACK")
                        TimeUnit.MILLISECONDS.sleep(400)
                        startInput(i + 1, endPoint)
                    }
                }
            }
            TimeUnit.MILLISECONDS.sleep(700)

//            //нажатие на "крестик"
//            println("sss нажатие на крестик (130 - 150) ---> ${adbShellInput("dumpsys meminfo io.faceapp")}")
//            adbShellInput("input tap 72 115")
//            TimeUnit.MILLISECONDS.sleep(300)

            //нажатие бэкспэйс 3 раза
//            for (j in 1..3) {
//                adbShellInput("input keyevent KEYCODE_BACK")
//                TimeUnit.MILLISECONDS.sleep(400)
//            }

            println(
                "sss ______________________________ " +
                        " конец ${adbShellInput("dumpsys meminfo io.faceapp")}," +
                        " убили ${adbShellInput("am force-stop io.faceapp")}, " +
                        "фото номер:  $i"
            )

            if( i == endPoint){
                stopSelf()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "service stopped", Toast.LENGTH_LONG).show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
