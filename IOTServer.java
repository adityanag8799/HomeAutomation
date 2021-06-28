/*
Board is registering: BR,Blue,Fan,Cooler,Ac#
Board is asking for commands : BC,Blue#
Command Center is asking for list CC,ls
Command Center is issuing a command CC,CMD,Turn on the fan connected to Blue
				CC,CMD,Blue,Fan,1#
				Response:
				0 if both device name and board name are incorrect
				1 if device name is incorrect
				2 if both name is correct (Command Accepted)


*/
import java.util.*;
import java.io.*;
import java.net.*;
class Board
{
public String id;
public List<String> electronicUnits;
}
class IOTServer
{
private ServerSocket serverSocket;
public List<Board> boards;
public Map<String,List<String>> commands;
IOTServer()
{
boards=new LinkedList<>();
commands=new HashMap<>();
}
public void start()
{
try
{
serverSocket=new ServerSocket(5050);
InputStream inputStream;
OutputStream outputStream;
InputStreamReader inputStreamReader;
OutputStreamWriter outputStreamWriter;
StringBuffer stringBuffer;
int x,i;
String boardId;
String command;
List<String> electronicUnits;
List<String> commandsList;
String electronicUnit;
String splits[];
Socket socket;
String request,response;
Board board;
String origin;
String boardName;
String deviceName;
boolean boardNameFound;
boolean deviceNameFound;
while(true)
{
System.out.println("Server is ready to serve at port number 5050 ........");
socket=serverSocket.accept();
inputStream=socket.getInputStream();
inputStreamReader=new InputStreamReader(inputStream);
stringBuffer=new StringBuffer();
while(true)
{
x=inputStreamReader.read();
if(x=='#') break;
stringBuffer.append((char)x);
}
request=stringBuffer.toString();
System.out.println("Request : "+request);
splits=request.split(",");
origin=splits[0];
if(origin.equals("BR"))
{
boardId=splits[1];
i=2;
electronicUnits=new LinkedList<>();
while(i<splits.length)
{
electronicUnit=splits[i];
electronicUnits.add(electronicUnit);
i++;
}
board=new Board();
board.id=boardId;
board.electronicUnits=electronicUnits;
boards.add(board);
response="GOT IT#";
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
System.out.println("BR Response sent :"+response);
}else
if(origin.equals("BC")) // board is asking for commands
{
boardName=splits[1];
response="";
if(commands.containsKey(boardName))
{
commandsList=commands.get(boardName);
if(commandsList.size()>0)
{
for(String cmd:commandsList)
{
if(response.length()>0) response=response+",";
response=response+cmd;
}
commandsList.clear(); // ***
}
}
response=response+"#";
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
System.out.println("BC Response sent :"+response);
continue;
}else
if(origin.equals("CC"))
{
command=splits[1];
if(command.equals("CMD"))
{
boardName=splits[2];
deviceName=splits[3];
//validations is to be applied
boardNameFound=false;
deviceNameFound=false;
for(x=0;x<boards.size();x++)
{
board=boards.get(x);
if(board.id.equals(boardName))
{
boardNameFound=true;
for(i=0;i<board.electronicUnits.size();i++)
{
if(deviceName.equals(board.electronicUnits.get(i)))
{
deviceNameFound=true;
break;
}
}
break;
}
}
response="";
if(boardNameFound && deviceNameFound)
{
command=splits[3]+","+splits[4];
commandsList=commands.get(boardName);
if(commandsList==null)
{
commandsList=new LinkedList<>();
commands.put(boardName,commandsList);
}
commandsList.add(command);
response="2#";
}else
if(boardNameFound==false && deviceNameFound==false)
{
response="0#";
}else
if(boardNameFound && deviceNameFound==false)
{
response="1#";
}
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
continue;
}
if(command.equals("ls"))
{
response="";
i=0;
for(Board b : boards)
{
i++;
response=response+b.id;
for(String eu:b.electronicUnits)
{
response=response+","+eu;
}
if(i<boards.size()) response=response+"!";
}
response=response+"#";
outputStream=socket.getOutputStream();
outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(response);
outputStreamWriter.flush();
socket.close();
continue;
} // ls ends 
} //else
continue;
} // while
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
} // start ends
public static void main(String gg[])
{
IOTServer iotServer=new IOTServer();
iotServer.start();
}
} // class