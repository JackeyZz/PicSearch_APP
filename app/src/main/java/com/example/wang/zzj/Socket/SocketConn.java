package com.example.wang.zzj.Socket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by acer on 2017/4/4.
 */
public class SocketConn {

    public void SocketConn(){
        InitView();
    }
    public void InitView()
    {
        String ip = "192.168.191.1";
        Bitmap bmp;
       int port = 1818;
        ImageView image = null;
        try {
            //传输数据
            Socket socket = null;
            socket = new Socket(ip, port);
            //BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //writer.write(msg);
            //writer.flush();
            //writer.close();
            //接收图像
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
            int size = dataInput.readInt();
            byte[] data = new byte[size];
            int len = 0;
            while (len < size) {
                len += dataInput.read(data, len, size - len);
            }
            ByteArrayOutputStream outPut = new ByteArrayOutputStream();
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outPut);
            image.setImageBitmap(bmp);

            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
