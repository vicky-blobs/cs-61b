/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 =
            "^(0[0-9]|1[0-2]|[1-9])\\/(0[1-9]|1\\d|2\\d|3[01]|[0-9])\\/(19|20)\\d{2}$";

    /** Pattern to match 61b notation for literal IntLists. */
    public static String P2 =
            "^\\(\\d+(?:[ \\t]*,[ \\t]*\\d+)+\\)$";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static String P3 = "";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 =
            "^[a-z0-9][a-z0-9-_]{0,61}[a-z0-9]{0,1}\\.(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})$";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    
}
