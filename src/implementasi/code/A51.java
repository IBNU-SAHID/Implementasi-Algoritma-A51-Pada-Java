package implementasi.code;

import java.util.Scanner;

class A51 {

    int[] x = new int[19]; //register x
    int[] y = new int[22]; //register y
    int[] z = new int[23]; //register z



    public void loadRegisters(String key) {
        int i; //counter untuk looping
        //inisialisasi register x
        for (i = 0; i < 19; i++) {
            x[i] = Integer.parseInt(key.substring(i, i + 1)); //mengambil nilai bit ke-i dari key
        }
        //inisialisasi register y
        for (i = 0; i < 22; i++) {
            y[i] = Integer.parseInt(key.substring(19 + i, 19 + i + 1)); //mengambil nilai bit ke-i dari key
        }
        //inisialisasi register z
        for (i = 0; i < 23; i++) {
            z[i] = Integer.parseInt(key.substring(19 + 22 + i, 19 + 22 + i + 1)); //mengambil nilai bit ke-i dari key
        }
    }

    // set key
    public boolean setKey(String key) {
        //jika key berjumlah 64 bit dan hanya berisi 0 dan 1
        if (key.length() == 64 && key.matches("[01]+")) {
            this.loadRegisters(key); //inisialisasi register x, y, z
            return true; //mengembalikan nilai true
        }
        return false; //mengembalikan nilai false
    }

    // mengambil nilai bit ke-i dari register x, y, z
    public int[] getKeystream(int length) {
        int[] keystream = new int[length]; //array untuk menyimpan nilai bit ke-i dari register x, y, z
        int[] t = new int[8]; //array untuk menyimpan nilai bit ke-i dari register x, y, z

        for (int i = 0; i < length; i++) {

            t[0] = (x[8] ^ x[13] ^ x[16] ^ x[17]);
            t[1] = (y[10] ^ y[20]);
            t[2] = (z[10] ^ z[10] ^ z[10] ^ z[21]);
            t[3] = (t[0] ^ t[1] ^ t[2]);
            t[4] = (x[13] ^ x[16]);
            t[5] = (y[10] ^ y[20]);
            t[6] = (z[7] ^ z[20]);
            t[7] = (t[4] ^ t[5] ^ t[6]);

            for (int j = 0; j < 18; j++) {
                x[j] = x[j + 1]; //geser bit ke-i register x ke bit ke-i+1
            }
            x[18] = t[3];

            for (int j = 0; j < 21; j++) {
                y[j] = y[j + 1]; //geser bit ke-i register y ke bit ke-i+1
            }
            y[21] = t[3];


            for (int j = 0; j < 22; j++) {
                z[j] = z[j + 1]; //geser bit ke-i dari register z ke bit ke-i+1
            }
            z[22] = t[3];

            keystream[i] = t[7];
        }
        return keystream;
    }

    //mengenkripsi pesan
    public String encrypt(String plaintext) {
        StringBuilder s = new StringBuilder(); //untuk menyimpan pesan yang telah dienkripsi
        int[] binary = this.toBinary(plaintext); //mengubah pesan ke dalam bentuk biner
        int[] keystream = getKeystream(binary.length); //mengambil nilai bit ke-i dari register x, y, z
        //mengenkripsi pesan
        for (int i = 0; i < binary.length; i++) {
            s.append(binary[i] ^ keystream[i]); //menyimpan pesan yang telah dienkripsi
        }
        return s.toString(); //mengembalikan pesan yang telah dienkripsi
    }

    // method untuk decrypt cipher text
    public String decrypt(String ciphertext) {
        StringBuilder s = new StringBuilder(); // untuk menyimpan hasil decrypt
        int[] binary = new int[ciphertext.length()]; // untuk menyimpan nilai binary dari ciphertext
        int[] keystream = getKeystream(ciphertext.length()); // untuk menyimpan nilai binary dari keystream

        for (int i = 0; i < binary.length; i++) { // looping sebanyak panjang ciphertext
            binary[i] = Integer.parseInt(ciphertext.substring(i, i + 1)); // mengubah nilai ciphertext ke binary
            s.append(binary[i] ^ keystream[i]); // melakukan operasi XOR antara ciphertext dan keystream
        }
        return this.toStr(s.toString()); // mengembalikan nilai hasil decrypt
    }

    //mengubah plaintext menjadi binary
    public int[] toBinary(String plaintext) {
        int[] binary = new int[plaintext.length() * 8]; // untuk menyimpan nilai binary dari plaintext

        // mengubah plaintext ke binary
        for (int i = 0; i < plaintext.length(); i++) { // looping sebanyak panjang plaintext
            String s = Integer.toBinaryString(plaintext.charAt(i)); // mengubah nilai plaintext ke binary
            while (s.length() < 8) { // jika panjang binary kurang dari 8, maka tambahkan 0 di depannya
                s = "0" + s; // tambahkan 0 di depan binary
            }
            // menyimpan nilai binary ke array
            for (int j = 0; j < 8; j++) { //
                binary[i * 8 + j] = Integer.parseInt(s.substring(j, j + 1)); // menyimpan nilai binary ke array
            }
        }
        return binary; // mengembalikan nilai binary
    }

    // method untuk mengubah binary ke String
    public String toStr(String binary) { // parameter binary adalah nilai binary yang akan diubah ke String
        StringBuilder s = new StringBuilder(); // untuk menyimpan nilai String
        // mengubah binary ke String
        for (int i = 0; i < binary.length() / 8; i++) { // looping sebanyak panjang binary dibagi 8
            String str = binary.substring(i * 8, i * 8 + 8); // mengambil nilai binary sepanjang 8
            s.append((char) Integer.parseInt(str, 2)); // mengubah nilai binary ke String
        }
        return s.toString(); // mengembalikan nilai String
    }

    // menu
    static void printOptions() {
        System.out.println("=============================");
        System.out.println("Kelompok 8 Keamanan informasi");
        System.out.println("------------------------------");
        System.out.println("* 1: Encrypt *");
        System.out.println("* 2: decrypt *");
        System.out.println("* q: Keluar *");
        System.out.println("------------------------------");
    }

    public static void main(String[] args) {

        A51 a51 = new A51(); // membuat objek A51
        Scanner input = new Scanner(System.in); // untuk inputan user

        printOptions(); // menampilkan menu
        String option = input.nextLine(); // mengambil inputan user menu

        // contoh key : 1101000111000111000010011010101010010110101001100111100101101001
        // looping untuk memilih menu
        while (!option.equals("q")) {
            if (option.equals("1")) { // jika user memilih menu 1
                // Encrypt
                // mengambil inputan key
                System.out.println("Masukan key (64 bits):");
                String key = input.nextLine();
                // mengambil inputan plaintext
                if (a51.setKey(key)) { // jika key yang diinputkan valid
                    System.out.println("Masukan  plaintext:"); // mengambil inputan plaintext
                    String plaintext = input.nextLine(); // mengambil inputan plaintext
                    String ciphertext = a51.encrypt(plaintext); // mengenkripsi plaintext
                    System.out.println("Ciphertext: " + ciphertext); // menampilkan ciphertext
                } else { // jika key yang diinputkan tidak valid
                    System.out.println("key tidak valid!"); // menampilkan pesan key tidak valid
                }
            } else if (option.equals("2")) { // jika user memilih menu 2
                //decrypt
                // mengambil inputan key
                System.out.println("Masukan key (64 bits):");
                String key = input.nextLine();
                if (a51.setKey(key)) { // jika key yang diinputkan valid
                    System.out.println("Masukan ciphertext:"); //
                    String ciphertext = input.nextLine(); // mengambil inputan ciphertext
                    String plaintext = a51.decrypt(ciphertext); // mendekripsi ciphertext
                    System.out.println("Plaintext: " + plaintext); // menampilkan plaintext
                    System.out.println(" ");
                } else { // jika key yang diinputkan tidak valid
                    System.out.println("key tidak valid!"); // menampilkan pesan key tidak valid
                }
            } else { // jika user memilih menu selain 1 dan 2
                System.out.println(" opsi pilihan tidak ada!"); // menampilkan pesan opsi pilihan tidak ada
            }
            printOptions(); // menampilkan menu
            option = input.nextLine(); // mengambil inputan user menu
        }
    }
}