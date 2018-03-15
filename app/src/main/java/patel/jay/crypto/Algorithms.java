package patel.jay.crypto;


import android.support.annotation.Nullable;

public class Algorithms {

    private String text, sIV, sIV2;

    private final int size = 8;

    public Algorithms(String sIV, String sIV2, String text) {
        this.sIV = sIV;
        this.sIV2 = sIV2;
        this.text = text;
    }

    public String lfsrQue(String str) {
        String output = "Que: ";

        int[] queue = lfsr(str);
        assert queue != null;
        for (int aQueue : queue) {
            output += aQueue + " ";
        }
        return output;

    }

    @Nullable
    public int[] lfsr(String str) {
        try {
            double size = Math.pow(2, str.length()) - 1;
            int[] queue = new int[(int) size];
            for (int i = str.length() - 1, j = 0; i >= 0; i--) {
                int n = Integer.parseInt(str.charAt(i) + "");
                queue[j++] = n;
            }
            for (int i = str.length(); i < size; i++) {
                int n = (queue[i - str.length()] + queue[i - str.length() + 1]) % 2;
                queue[i] = n;
            }

            return queue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ECB() {
        try {
            String output = "", cBit = "";
            int[] iv = lfsr(sIV);
            int plain[], ascii;

            for (int i = 0; i < text.length(); i++) {
                cBit = "";
                char c = text.charAt(i);
                plain = set8(Integer.toBinaryString(c));

                for (int j = 0; j < plain.length; j++) {
                    assert iv != null;
                    int t = (iv[j] + plain[j]) % 2;
                    cBit += t + "";
                }

                ascii = Integer.parseInt(cBit, 2);
                output += (char) ascii;
            }

            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String CBC(boolean isEnDn) {
        try {
            String output = "", cBit = "";

            int plain[], ascii,
                    iv[] = lfsr(sIV),
                    key[] = lfsr(sIV2);

            for (int i = 0; i < text.length(); i++) {
                cBit = "";
                char c = text.charAt(i);
                plain = set8(Integer.toBinaryString(c));

                for (int j = 0; j < plain.length; j++) {
                    assert iv != null;
                    assert key != null;

                    int ePT = (iv[j] + key[j]) % 2;
                    int t = (ePT + plain[j]) % 2;
                    cBit += t + "";

                    if (isEnDn) {
                        iv[j] = t;
                    } else {
                        iv[j] = plain[j];
                    }
                }

                ascii = Integer.parseInt(cBit, 2);
                output += ((char) ascii);
            }

            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String CFM(boolean isEnDn) {
        try {
            String output = "", cBit = "";

            int plain[], ascii,
                    iv[] = lfsr(sIV),
                    key[] = lfsr(sIV2);

            for (int i = 0; i < text.length(); i++) {
                cBit = "";
                char c = text.charAt(i);
                plain = set8(Integer.toBinaryString(c));

                for (int j = 0; j < plain.length; j++) {
                    assert iv != null;
                    assert key != null;
                    int ePT = (iv[j] + key[j]) % 2;
                    int t = (ePT + plain[j]) % 2;
                    cBit += t + "";

                    if (isEnDn) {
                        iv[j] = t;
                    } else {
                        iv[j] = plain[j];
                    }
                }

                ascii = Integer.parseInt(cBit, 2);
                output += ((char) ascii);
            }

            return output;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String OFM() {
        try {
            String output = "", cBit = "";

            int[] iv1 = lfsr(sIV);
            int[] key = lfsr(sIV2);
            int plain[], ascii;

            for (int i = 0; i < text.length(); i++) {
                cBit = "";
                char c = text.charAt(i);
                plain = set8(Integer.toBinaryString(c));

                for (int j = 0; j < plain.length; j++) {
                    assert iv1 != null;
                    assert key != null;
                    int eIv = (iv1[j] + key[j]) % 2;

                    int xor = (eIv + plain[j]) % 2;
                    cBit += xor;
                    iv1[j] = eIv;
                }

                ascii = Integer.parseInt(cBit, 2);
                output += ((char) ascii);
            }

            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String CTR() {
        try {
            int counter = 0;
            String output = "", ctrBit = "", cBits = "";

            int iv[], pBit[], ascii, key[] = lfsr(sIV2);

            for (int i = 0; i < text.length(); i++) {
                cBits = "";

                iv = new int[size];
                for (int j = 0; j < 4; j++) {
                    iv[j] = Integer.parseInt(sIV.charAt(j) + "");
                }

                ctrBit = Integer.toBinaryString(counter);
                for (int k = size, j = ctrBit.length() - 1; j >= 0; j--) {
                    iv[--k] = Integer.parseInt(ctrBit.charAt(j) + "");
                }

                char ch = text.charAt(i);
                pBit = set8(Integer.toBinaryString(ch));

                for (int m = 0; m < pBit.length; m++) {

                    assert key != null;
                    int ePT = (iv[m] + key[m]) % 2;

                    int t = (ePT + pBit[m]) % 2;
                    cBits += t + "";

                }

                ascii = Integer.parseInt(cBits, 2);
                output += (char) ascii;

                if (counter < 15) {
                    counter++;
                } else {
                    counter = 0;
                }
            }

            return output;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public int[] set8(String pBit) {
        int plain[] = new int[size];
        for (int k = size, j = pBit.length() - 1; j >= 0; j--) {
            plain[--k] = Integer.parseInt(pBit.charAt(j) + "");
        }

        return plain;
    }
}