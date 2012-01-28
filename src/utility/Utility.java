package utility;

import java.text.DecimalFormat;

public class Utility {


	/**
	 * Converts to two decimal places
	 * @param num
	 * @return
	 */
	public static float formatTwoDecimal(float num) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Float.valueOf(twoDForm.format(num));
	}
}
