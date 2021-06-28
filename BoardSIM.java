/*
BR,BLUE,AC,Cooler#

*/
import java.io.*;
import java.net.*;
import java.util.*;
class BoardSIM
{
public static void main(String gg[])
{
if(gg.length<2) 
{
System.out.println("Usage : java BoardSIM boardsId list of names of devices connected to boardId");
return;
}
try
{
String request,response;
request="BR";
int x=0;
while(x<gg.length)
{
request=request+","+gg[x];
x++;
}
request=request+"#";
Socket socket=new Socket("localhost",5050);
OutputStream outputStream=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(request);
outputStreamWriter.flush();
InputStream inputStream=socket.getInputStream();
InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
StringBuffer stringBuffer=new StringBuffer();
x=0;
while(true)
{
x=inputStreamReader.read();
if(x=='#') break;
stringBuffer.append((char)x);
}
response=stringBuffer.toString();
System.out.println(response);
socket.close();

// gg[0] contains boardId
//gg[1] contains name of device it controls
//gg[2] .....................

String splits[];
String deviceName;
String command;

while(true)
{
try
{
Thread.sleep(5000);   // 5000 miliseconds
}catch(Exception ie)
{
}
socket=new Socket("localhost",5050);
request="BC,"+gg[0]+"#";
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(request);
outputStreamWriter.flush();
inputStream=socket.getInputStream();
inputStreamReader=new InputStreamReader(inputStream);
stringBuffer=new StringBuffer();
while(true)
{
x=inputStreamReader.read();
if(x=='#') break;
stringBuffer.append((char)x);
}
socket.close();
response=stringBuffer.toString();
if(response.length()==0)
{
System.out.println("Nothing to do");
continue;
}
splits=response.split(",");
x=0;
while(x<splits.length)
{
deviceName=splits[x];
x++;
command=splits[x];
x++;
if(command.equals("0")) System.out.println("Turning off : "+deviceName);
if(command.equals("1")) System.out.println("Turning on : "+deviceName);
}
}
}catch(Exception e)
{
System.out.println(e);
}
}
}