package com.example.anoopm.mqtt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.anoopm.mqtt.manager.MQTTConnectionParams
import com.example.anoopm.mqtt.manager.MQTTmanager
import com.example.anoopm.mqtt.protocols.UIUpdaterInterface
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject

class MainActivity : AppCompatActivity(), UIUpdaterInterface {

    var mqttManager:MQTTmanager? = null

    // Interface methods
    override fun resetUIWithConnection(status: Boolean) {

        ipAddressField.isEnabled  = !status
        topicField.isEnabled      = !status
        messageField.isEnabled    = status
        connectBtn.isEnabled      = !status
        sendBtn.isEnabled         = status

        // Update the status label.
        if (status){
            updateStatusViewWith("Connected")
        }else{
            updateStatusViewWith("Disconnected")
        }
    }

    override fun updateStatusViewWith(status: String) {
        statusLabl.text = status
    }

    override fun update(message: String) {
        println("Fun was called")
        var text = messageHistoryView.text.toString()
        var newText = """
            $text
            $message
            """
        //var newText = text.toString() + "\n" + message +  "\n"
        messageHistoryView.setText(newText)
        messageHistoryView.setSelection(messageHistoryView.text.length)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable send button and message textfield only after connection
        resetUIWithConnection(false)
    }


    fun connect(view: View){

        if (!(ipAddressField.text.isNullOrEmpty() && topicField.text.isNullOrEmpty())) {
            var host = "tcp://" + ipAddressField.text.toString() + ":1883"
            var topic = topicField.text.toString()
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,"","")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()
        }else{
            updateStatusViewWith("Please enter all valid fields")
        }

    }

    fun sendMessage(view: View){
        var topicSend = "IMU/reset"
        mqttManager?.publish(topicSend.toString(),messageField.text.toString())

    }

    fun sendUweMessage(topic: String, message: String){
        // mqttManager?.publish()


        //messageField.setTopic
        var topicSend = "IMU/offset"
        //mqttManager?.publish(topicSend.toString(),messageField.text.toString())


    }


    fun imuOffset(view: View){
        // mqttManager?.publish()

       // messageField.setText("Mark is the king" + mqttManager.pitchFor.toString())
        //messageField.setTopic
        var offsetJson= JSONObject()
        offsetJson.put("ptich",mqttManager?.pitch.toString() )
        offsetJson.put("roll",mqttManager?.pitch.toString() )
        var topicSend = "IMU/offset"
        var msg = mqttManager?.pitch.toString() + mqttManager?.roll.toString()
        mqttManager?.publish(topicSend.toString(), offsetJson.toString())


    }


}
