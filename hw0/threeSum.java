public class threeSum {
    public static void main(String[] args) {
        int[] boop = new int[] {5, 1, 0, 3, 6};
        int[] beep = new int[] {8, 2, -1, -1, 15};
        System.out.println("three sum possible?: " + threeSum(boop));
        System.out.println("three sum possible?: " + threeSum(beep));

    }
    public static boolean threeSum(int[] A) {
        //3sum9
        int arr_size = A.length;
        for (int i = 0; i < arr_size - 2; i++) {
            for (int j = i; j < arr_size - 1; j++) {
                for (int k = j; k <arr_size; k++) {
                    if (A[i] + A[j] + A[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}