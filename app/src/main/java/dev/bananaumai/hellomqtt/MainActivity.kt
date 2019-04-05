package dev.bananaumai.hellomqtt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient


class MainActivity : AppCompatActivity() {

    lateinit var client : MqttAndroidClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttConnect()
    }

    private fun mqttConnect() {
        client = MqttAndroidClient(
            applicationContext,
            "tcp://10.0.2.2:1883", MqttClient.generateClientId()
        )

        client.connect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "connection failed")
                throw exception!!
            }
        })
    }

    fun mqttPublish(view: android.view.View) {
        if (client.isConnected) {
            Log.d("MQTT", "publish message!")
            client.publish("test/topic", "Hello MQTT".toByteArray(), 0, true)
            client.close()
        } else {
            Log.d("MQTT", "not connected...")
        }
    }
}
