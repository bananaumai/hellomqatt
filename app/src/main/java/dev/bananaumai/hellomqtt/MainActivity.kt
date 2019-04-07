package dev.bananaumai.hellomqtt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient


class MainActivity : AppCompatActivity() {

    lateinit var client : MqttAndroidClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        // Can't call method depending on "real" MQTT connection, like MqttAndroidClient.publish
        //
        // client.publish("test/topic", "Hello MQTT".toByteArray(), 0, true)
        //
        // That is because "real" connection is created asynchronously.
        // This is same even though add Thread.sleep here.
        // This is because the callback method witch create "reaL" MQTT connection is called in the main thread.
        // To understand this behavior, you need to understand Android's Service life cycle.
        // ( To know the paho's Android MQTT client is using android Service, see the source code of MqttAndroidClient.connect.
    }

    fun View.mqttPublish() {
        if (client.isConnected) {
            Log.d("MQTT", "publish message!")
            client.publish("test/topic", "Hello MQTT".toByteArray(), 0, true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.close()
    }
}
