
public class LucaUtils {
	/*
	DAVID: HE BORRADO LA VARIABLE ROLE_MANAGER; 	
	*/
	public static String ROLE_ADMIN = "ROLE_ADMIN";
	public static String ROLE_SMO = "ROLE_SMO";
	public static String ROLE_RRHH = "ROLE_RRHH";

	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	
	private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

	public static String generateRandomString (int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) 
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
	
	public static Employee getLoggedUser() {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employee;
	}
	
	public static Site getLoggedSite() {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (employee == null) {
			return null;
		}
		
		return employee.getSite();
	}
	
	public static boolean loggedUserIsManager() {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employee.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_MANAGER));
	}
	
	public static boolean loggedUserIsAdmin() {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employee.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN));
	}
	
	public static boolean loggedUserIsSMO() {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employee.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_SMO));
	}
	
	public static boolean loggedUserIsRRHH() {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employee.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_RRHH));
	}
	
	public static boolean loggedUserIsAdminOrManager() {
		return loggedUserIsAdmin() || loggedUserIsManager();
	}
	
	public static boolean hasErrorInField (List<FieldError> errors, String fieldName) {
	    for (FieldError error : errors ) {
	       if (fieldName.equals(error.getField())) {
	    	   return true;
	       }
	    } 
	    return false;
	}
	
	public static String getFieldErrorDescription (List<FieldError> errors, String fieldName) {
	    StringBuilder errorString = new StringBuilder();
		for (FieldError error : errors ) {
	       if (fieldName.equals(error.getField())) {
	    	   errorString.append(error.getDefaultMessage()).append(". ");
	       }
	    } 
	    return errorString.toString();
	}
	
	public static void updateLoggedUserNotificationNumber(int absences, int absencesOwned, int delegations) {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		employee.setPendingNotifications(absences + delegations + absencesOwned);
		employee.setPendingAbsences(absences);
		employee.setPendingOwnAbsences(absencesOwned);
		employee.setPendingDelegations(delegations);
		return;
	}
	
	public static void updateLoggedViews(boolean absenceViews) {
		Employee employee = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		employee.setAbsenceViews(absenceViews);
		return;
	}

	public static final int getPendingHolidaysEmployee(Employee employee) {
		if (employee == null || employee.getAnnualHolidays() == null) {
			return 0;
		}
		long pendingDays = (employee.getAnnualHolidays() * (365 - LocalDate.now().getDayOfYear())) / 365;
		return Math.toIntExact(pendingDays);
	}
	
	public static final Resource getImageFromClassPath(String imageName) {
		Resource resource = new ClassPathResource("/public/images/" + imageName);
		return resource;
	}
	
	public static Integer[] addElementToIntArray(Integer[] a, int e) {
		if (a == null) {
			return new Integer[] {e};
		}
	    a = Arrays.copyOf(a, a.length + 1);
	    a[a.length - 1] = e;
	    return a;
	}
	
	public static boolean validateUserInAction(Employee user) {
		if (user == null) {
			return false;
		}
		
		// Get logged user
		Employee loggedUser = LucaUtils.getLoggedUser();
		if (loggedUser == null) {
			return false;
		}
		
		if (loggedUser.getId() != user.getId()) {
			return false;
		}
		
		return true;
	}
	
	public static String capitalizeString(String string) {
		if (string == null || string.isEmpty()) {
			return "";
		}
		
		char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
				found = false;
			}
		}
		return String.valueOf(chars);
	}
	
	public static boolean isNullOrEmpty(String validateString) {
		if (validateString == null) {
			return true;
		}
		if (validateString.trim().isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean validateHexColor(String colorCode) {
		if (isNullOrEmpty(colorCode)) {
			return false;
		}
		
		Pattern pattern = Pattern.compile(HEX_PATTERN);
		Matcher matcher = pattern.matcher(colorCode);
	    return matcher.matches();
	}
	
	public static String getStringErrorFromList(List<String> errors) {
		StringBuilder result = new StringBuilder();
		if (errors != null && !errors.isEmpty()) {
			for (String error : errors) {
				result.append(error).append(System.lineSeparator());
			}
		}
		return result.toString();
	}
	
	//A ... le gusta comerme el brazo
}
