package ubiquigame.common.controllerMessages;


import java.io.*;

public class NetworkPackage implements Serializable {

	private static final long serialVersionUID = 8000727144882059650L;
	private NetworkMessage networkMessage;

    public NetworkPackage(NetworkMessage networkMessage) {
        this.networkMessage = networkMessage;
    }

    public NetworkMessage getNetworkMessage() {
        return networkMessage;
    }

    /**
     * Deserializes a {@link NetworkPackage} from a Byte Array
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static NetworkPackage fromBytes(byte[]bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        NetworkPackage networkPackage = (NetworkPackage) objectInputStream.readObject();

        objectInputStream.close();
        byteArrayInputStream.close();

        return networkPackage;
    }


    /**
     * Serializes a {@link NetworkPackage} from a Byte Array
     * @return
     * @throws IOException
     */
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    try {
        objectOutputStream.writeObject(this);
    }catch(NotSerializableException e){
        e.printStackTrace();
    }
        byte[] bytes = byteArrayOutputStream.toByteArray();

        objectOutputStream.close();
        byteArrayOutputStream.close();

        return bytes;
    }

}
