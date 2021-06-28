import java.io.*;
import java.net.*;
import java.util.*;
class IOTController
{
public static void main(String gg[])
{
try
{
Socket socket;
InputStream inputStream;
InputStreamReader inputStreamReader;
OutputStream outputStream;
OutputStreamWriter outputStreamWriter;
StringBuffer stringBuffer;
int x,i;
String splits1[];
String splits2[];
String request,response;
String boardId;
String electronicUnit;
String command;
String board;
String device;
String str;
InputStreamReader keyboardInputStreamReader=new InputStreamReader(System.in);
BufferedReader bufferedReader=new BufferedReader(keyboardInputStreamReader);
while(true)
{
System.out.print("iot-Controller>");
command=bufferedReader.readLine();
command=command.trim();
while(command.indexOf("  ")!=-1) command=command.replaceAll("  "," ");
if(command.equalsIgnoreCase("quit")) break;
if(command.equalsIgnoreCase("help"))
{
System.out.println("    ls : to display the list of electronics units.");
System.out.println("    quit : to exit.");
System.out.println("    Turn on device Id connected to board Name.");
continue;
}
if(command.equals("ls"))
{
request="CC,ls#";
socket=new Socket("localhost",5050);
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
response=stringBuffer.toString();
socket.close();
if(response==null || response.length()==0) 
{
System.out.println("No Boards connected to Server");
continue;
}
splits1=response.split("!");
for(x=0;x<splits1.length;x++)
{
splits2=splits1[x].split(",");
System.out.println("-------------------------");
System.out.println("Board  : "+splits2[0]);
System.out.println("-------------------------");
for(i=1;i<splits2.length;i++)
{
System.out.println(i+" ) "+splits2[i]);
}
}
System.out.println();
continue;
}
str=command.toUpperCase();
if(str.startsWith("TURN ON ") || str.startsWith("TURN OFF "))
{
x=str.indexOf(" CONNECTED TO ");
if(x==-1)
{
System.out.println("Invalid command type help .");
continue;
}
if(str.startsWith("TURN ON")) i=8; else i=9;
device=command.substring(i,x);
board=command.substring(x+14);
request="CC,CMD,"+board+","+device+",";
if(i==8) request=request+"1#";
if(i==9) request=request+"0#";
socket=new Socket("localhost",5050);
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
response=stringBuffer.toString();
socket.close();
if(response.equals("0"))
{
System.out.println("Board Name : "+board+" and device name : "+device+" are incorrect.");
}else 
if(response.equals("1"))
{
System.out.println("Device name  is incorrect."+device);
}else 
if(response.equals("2"))
{
System.out.println("Request Accepted");
}
continue;
}
System.out.println("Invalid command , type help for list of commands.");
}
System.out.println("Thank you for using command Center Software.");
}catch(Exception e)
{
System.out.println(e);
}
}
}