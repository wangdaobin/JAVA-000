import java.io.*;
import java.lang.reflect.Method;

/**
 * @Description : 自定义类加载器
 * @Author : WDB
 * @Date: 2020-10-20 20:55
 */
public class MyClassLoader extends ClassLoader{

    private String path;

    public MyClassLoader(String path){
        this.path = path;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        byte[] bytes = getBytes(path);
        if (bytes != null) {
            // 将class的字节码数组转换成Class类的实例
            result = defineClass(name, bytes, 0, bytes.length);
        }
        return result;
    }

    /**
     * 获取指定文件名称字节码（指定目录下）
     * @param path 文件路径
     * @return 文件字节数组
     */
    private byte[] getBytes(String path){
        File file = new File(path);
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();
                int currentByte = in.read();
                while(currentByte != -1){
                    //处理字节
                    out.write(255 - currentByte);
                    currentByte = in.read();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        String path = "E:\\Geek\\JAVA-000\\Week_01\\Hello.xlass";
        MyClassLoader myClassLoader = new MyClassLoader(path);
        try {
            Class<?> hello = myClassLoader.loadClass("Hello");
            System.out.println("类加载器是:" + hello.getClassLoader());
            Method method = hello.getDeclaredMethod("hello", null);
            Object object = hello.newInstance();
            method.invoke(object, null);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
