package upbox.util;
/**
 * Created by kang on 2015/8/5.
 * 支付宝支付参数
 */
public class AliPayTools {
    // 商户PID
    public static final String PARTNER = "2088911191424347";
    // 商户收款账号
    public static final String SELLER = "pay@ppsports.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM9Tqi/AIqsJowseoIB21tF76DHwi6t76ouyawL1iupg9Qvutk3pYDnfBs9ABE+523610BrinM1EN8nicb0P8i7kweYjVSrD4C3J8LtsEwC1Sh/63ygYkTIlVlQJHKaQ+CwchOG2DZPQRz2AuCL3G6Rx1y+9Zqp5tJVavh+5fYTtAgMBAAECgYBSAfMIbkSISQiQBm5QQjGYrR1cW5yzmRFebpV7lyp6qR1kueisisqAOaLU6aqK6qZSyZHmgIqrevdiSMrakOsnQPIOEZJxKFDI1rfH0scRw6NaJdEa70Sbo/qjbIIYt/z7w31ScGxo6wb5WASf5jtitlKj2JOz1PtOG2EqvgTrAQJBAPCIx/E4doSU2uYYf5z9c2y2OqEtGgSbYQzlNAq1T22RK+QQT9GLGca8FlgUPZFmXhpM+cfuZ/KD8KwI5DYrwsECQQDcqEkCYRgKYeW9/HOFOiUPuVDfP3kiLOmcPHmpNBW2gl44RFHDp9yR2EvBzthS3O9XscUcS19ExEeXFVGkU4ktAkBjygtROwCIbo0GCsHeqpOZVVyrg1+Y67FuvSRDEQdbyG9yFYZXw6K0/VEzx2nniZWeybKSzIiZZz2Q0buD8dHBAkBBDVfg3LEStoPdu+RuvZKZjR+7gWH74lPI3MddS96u3MZcPkAAt6c+VA8Zhxqda0cgimc7DTl784XLk9xq3rnNAkEAnzV2zwxxIZEufUmjXMPCepXkMIR6A9/MAb5G2OPBXoiZGFfxnukaXvHkfxiraUV/ZTigbQiDaXRlyMvRzL2lLg==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    
 //	public static final String CALLBACK="http://dev.upbox.com.cn:8091/appserver/alipay!successCallBackMethod.do"  ;
  
    public static final String CALLBACK="http://app.upbox.com.cn/appserver/alipay!successCallBackMethod.do";
    
    
 //   public static final String CALLBACK="http://dev.upbox.com.cn:8091/appserver/alipay!successCallBackMethod.do"  ;
}
