import java.nio.charset.Charset;

public class DesempaquetarExamen {
    public static void main(String args[]) throws Exception{
        Paquete readPack = PaqueteDAO.leerPaquete(args[0] + ".paquete");
        for (String blockName : readPack.getNombresBloque()) {
            byte[] block = readPack.getContenidoBloque(blockName);
            String blockContent = new String(block, Charset.forName("UTF-8"));
            System.out.println("\t"+blockName+": "+ blockContent.replace("\n", " "));
        }
        int a = 0;

    }
}
