package patel.jay.crypto;


import android.support.annotation.Nullable;

public class Algorithms {

    private final int size = 8;
    private boolean type = false;

    public Algorithms() {
    }

    public String lfsrQue(String str) {
        try {
            if (str.length() < 3) {
                throw new NumberFormatException("Enter More Than 2 Bits");
            }

            if (Integer.parseInt(str) == 0) {
                throw new NumberFormatException("Not Allow All 0");
            }

            double size = Math.pow(2, str.length()) - 1;
            int[] queue = new int[(int) size];

            for (int i = str.length() - 1, j = 0; i >= 0; i--) {
                int n = Integer.parseInt(str.charAt(i) + "");

                if (n > 1 || n < 0) {
                    throw new NumberFormatException("Enter Only 0 And 1");
                }

                queue[j++] = n;
            }

            for (int i = str.length(); i < size; i++) {
                int n = (Integer.parseInt(queue[i - str.length()] + "")
                        + Integer.parseInt(queue[i - str.length() + 1] + "")) % 2;
                queue[i] = n;
            }

            String output = "Que: ";

            for (int aQueue : queue) {
                output += aQueue + " ";
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
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

    public String CBC(String text, String sIV, String sIV2) {
        try {
            String output = "", cBit = "", pBit = "";

            switch (1) {
                case 1:
                    type = true;
                    break;

                case 2:
                    type = false;
                    break;

                default:
                    CBC(text, sIV, sIV2);
                    return "";
            }

            int plain[], ascii,
                    iv[] = lfsr(sIV),
                    key[] = lfsr(sIV2);

            for (int i = 0; i < text.length(); i++) {

                char c = text.charAt(i);
                cBit = "";
                pBit = Integer.toBinaryString(c);

                plain = new int[size];

                for (int k = size, j = pBit.length() - 1; j >= 0; j--) {
                    plain[--k] = Integer.parseInt(pBit.charAt(j) + "");
                }

                for (int j = 0; j < plain.length; j++) {
                    assert iv != null;
                    assert key != null;

                    int ePT = (iv[j] + key[j]) % 2;
                    int t = (ePT + plain[j]) % 2;
                    cBit += t + "";

                    if (type) {
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

    public String OFM(String text, String sIV, String sIv2) {
        try {
            String output = "", cBit = "", pBit = "";

            int[] iv1 = lfsr(sIV);
            int[] key = lfsr(sIv2);
            int plain[], ascii;

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);

                cBit = "";
                pBit = Integer.toBinaryString(c);

                plain = new int[size];

                for (int k = size, j = pBit.length() - 1; j >= 0; j--) {
                    plain[--k] = Integer.parseInt(pBit.charAt(j) + "");
                }

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

    public String CTR(String text, String nonce, String sIv2) {
        try {
            int counter = 0;
            String cT = "", ctrBit = "", oBits = "", iBits = "";

            int iv[], cBit[], ascii, key[] = lfsr(sIv2);

            for (int i = 0; i < text.length(); i++) {

                iv = new int[size];
                cBit = new int[size];
                ctrBit = Integer.toBinaryString(counter);

                for (int j = 0; j < 4; j++) {
                    iv[j] = Integer.parseInt(nonce.charAt(j) + "");
                }

                for (int k = size, j = ctrBit.length() - 1; j >= 0; j--) {
                    iv[--k] = Integer.parseInt(ctrBit.charAt(j) + "");
                }

                char ch = text.charAt(i);

                oBits = "";
                iBits = Integer.toBinaryString(ch);

                for (int k = size, j = iBits.length() - 1; j >= 0; j--) {
                    cBit[--k] = Integer.parseInt(iBits.charAt(j) + "");
                }

                for (int m = 0; m < cBit.length; m++) {

                    assert key != null;
                    int ePT = (iv[m] + key[m]) % 2;

                    int t = (ePT + cBit[m]) % 2;
                    oBits += t + "";

                }

                ascii = Integer.parseInt(oBits, 2);
                cT += (char) ascii;

                if (counter < 15) {
                    counter++;
                } else {
                    counter = 0;
                }
            }

            return cT;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String CFM(String text, String sIV, String sIV2) {
        try {
            String output = "", cBit = "", pBit = "";

            switch (1) {
                case 1:
                    type = true;
                    break;

                case 2:
                    type = false;
                    break;

                default:
                    CFM(text, sIV, sIV2);
                    return "";
            }

            int plain[], ascii,
                    iv[] = lfsr(sIV),
                    key[] = lfsr(sIV2);

            for (int i = 0; i < text.length(); i++) {

                char c = text.charAt(i);
                cBit = "";
                pBit = Integer.toBinaryString(c);

                plain = new int[size];

                for (int k = size, j = pBit.length() - 1; j >= 0; j--) {
                    plain[--k] = Integer.parseInt(pBit.charAt(j) + "");
                }

                for (int j = 0; j < plain.length; j++) {
                    assert iv != null;
                    assert key != null;
                    int ePT = (iv[j] + key[j]) % 2;
                    int t = (ePT + plain[j]) % 2;
                    cBit += t + "";

                    if (type) {
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

    public String ECB(String text, String sIv) {
        try {
            String output = "", cBit = "", pBit = "";
            int[] iv = lfsr(sIv);
            int plain[], ascii;

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                cBit = "";
                pBit = Integer.toBinaryString(c);

                plain = new int[size];
                for (int k = size, j = pBit.length() - 1; j >= 0; j--) {
                    plain[--k] = Integer.parseInt(pBit.charAt(j) + "");
                }

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

}