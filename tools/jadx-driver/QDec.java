import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QDec {
    public static void main(String[] args) throws Exception {
        // usage: java -cp <jadx-jar>:. QDec <apk> <outdir> <class> [<class>...]
        String apk = args[0];
        String outdir = args[1];
        Files.createDirectories(Paths.get(outdir));
        JadxArgs jargs = new JadxArgs();
        jargs.setInputFile(new File(apk));
        JadxDecompiler d = new JadxDecompiler(jargs);
        long t0 = System.currentTimeMillis();
        System.err.println("[QDec] loading " + apk);
        d.load();
        System.err.println("[QDec] loaded in " + (System.currentTimeMillis() - t0) / 1000 + "s");
        for (int i = 2; i < args.length; i++) {
            String cn = args[i];
            try {
                JavaClass c = d.searchJavaClassByOrigFullName(cn);
                if (c == null) { c = d.searchJavaClassOrItsParentByOrigFullName(cn); }
                if (c == null) { System.err.println("[QDec] NOT_FOUND " + cn); continue; }
                String code = c.getCode();
                String fname = cn.replace('.', '_').replace('$', '_') + ".java";
                Files.writeString(Path.of(outdir, fname), code == null ? "// null code" : code);
                System.err.println("[QDec] OK " + cn + " -> " + fname + " (" + (code == null ? 0 : code.length()) + " chars)");
            } catch (Exception e) {
                System.err.println("[QDec] FAIL " + cn + " : " + e);
            }
        }
        d.close();
        System.exit(0);
    }
}
