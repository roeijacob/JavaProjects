package exe3KnockKnock;

public class RuppinRegistrationProtocol {

    public enum Action {
        NONE,
        TRY_REGISTER,  
        TRY_LOGIN,     
        APPLY_UPDATE,  
        CLOSE
    }

    public static class Output {
        public final String message;   
        public final Action action;    

        public Output(String message, Action action) {
            this.message = message;
            this.action = action;
        }

        public static Output say(String msg) { return new Output(msg, Action.NONE); }
        public static Output act(Action action) { return new Output(null, action); }
        public static Output sayAndAct(String msg, Action action) { return new Output(msg, action); }
    }

    private enum Mode { NONE, REGISTER, LOGIN, UPDATE }
    private enum State {
        ASK_REGISTER,

        ASK_USERNAME,
        ASK_PASSWORD,

        ASK_ACADEMIC,
        ASK_YEARS,

        ASK_UPDATE_YN,

        UPD_PASS_YN,
        UPD_PASS_VAL,
        UPD_YEARS_YN,
        UPD_YEARS_VAL,
        UPD_STATUS_YN,
        UPD_STATUS_VAL,

        DONE
    }

    private Mode mode = Mode.NONE;
    private State state = State.ASK_REGISTER;

    private String username;
    private String password;
    private String academic; 
    private int years;

    private String newPassword = null;
    private Integer newYears = null;
    private String newAcademic = null;

    public Output processInput(String input) {
        if (input != null) input = input.trim();

        switch (state) {

            case ASK_REGISTER:
                if (input == null) return Output.say("Do you want to register? (yes/no)");
                if (isYes(input)) {
                    mode = Mode.REGISTER;
                    state = State.ASK_USERNAME;
                    return Output.say("Enter username:");
                }
                if (isNo(input)) {
                    mode = Mode.LOGIN;
                    state = State.ASK_USERNAME;
                    return Output.say("Enter username:");
                }
                return Output.say("Please answer yes/no.");

            case ASK_USERNAME:
                if (isEmpty(input)) return Output.say("Username cannot be empty. Enter username:");
                username = input;
                state = State.ASK_PASSWORD;
                return Output.say(mode == Mode.REGISTER
                        ? "Enter a strong password (>=9 chars, upper+lower+digit):"
                        : "Enter password:");

            case ASK_PASSWORD:
                if (isEmpty(input)) return Output.say("Password cannot be empty. Enter password:");

                if (mode == Mode.REGISTER) {
                    if (!isStrongPassword(input)) {
                        return Output.say("Password is not strong. Enter again (>=9 chars, upper+lower+digit):");
                    }
                    password = input;
                    state = State.ASK_ACADEMIC;
                    return Output.say("Enter academic status (STUDENT/TEACHER/OTHER):");
                }

                password = input;
                return Output.act(Action.TRY_LOGIN);

            case ASK_ACADEMIC:
                String st = normalizeStatus(input);
                if (st == null) return Output.say("Invalid academic status. Enter STUDENT/TEACHER/OTHER:");
                academic = st;
                state = State.ASK_YEARS;
                return Output.say("Enter years at Ruppin (0-90):");

            case ASK_YEARS:
                Integer y = parseYears(input);
                if (y == null) return Output.say("Invalid years. Please enter a number between 0 and 90:");
                years = y;
                return Output.act(Action.TRY_REGISTER);

            case ASK_UPDATE_YN:
                if (input == null) return Output.say("Do you want to update your details? (yes/no)");
                if (isNo(input)) {
                    state = State.DONE;
                    return Output.sayAndAct("Goodbye.", Action.CLOSE);
                }
                if (isYes(input)) {
                    mode = Mode.UPDATE;
                    state = State.UPD_PASS_YN;
                    return Output.say("Update password? (yes/no)");
                }
                return Output.say("Please answer yes/no.");

            case UPD_PASS_YN:
                if (isYes(input)) { state = State.UPD_PASS_VAL; return Output.say("Enter a strong password (>=9 chars, upper+lower+digit):"); }
                if (isNo(input)) { state = State.UPD_YEARS_YN; return Output.say("Update years at Ruppin? (yes/no)"); }
                return Output.say("Please answer yes/no.");

            case UPD_PASS_VAL:
                if (!isStrongPassword(input)) return Output.say("Password is not strong. Enter again (>=9 chars, upper+lower+digit):");
                newPassword = input;
                state = State.UPD_YEARS_YN;
                return Output.say("Update years at Ruppin? (yes/no)");

            case UPD_YEARS_YN:
                if (isYes(input)) { state = State.UPD_YEARS_VAL; return Output.say("Enter years at Ruppin (0-90):"); }
                if (isNo(input)) { state = State.UPD_STATUS_YN; return Output.say("Update academic status? (yes/no)"); }
                return Output.say("Please answer yes/no.");

            case UPD_YEARS_VAL:
                Integer yy = parseYears(input);
                if (yy == null) return Output.say("Invalid years. Please enter a number between 0 and 90:");
                newYears = yy;
                state = State.UPD_STATUS_YN;
                return Output.say("Update academic status? (yes/no)");

            case UPD_STATUS_YN:
                if (isYes(input)) { state = State.UPD_STATUS_VAL; return Output.say("Enter academic status (STUDENT/TEACHER/OTHER):"); }
                if (isNo(input)) {
                    return Output.act(Action.APPLY_UPDATE);
                }
                return Output.say("Please answer yes/no.");

            case UPD_STATUS_VAL:
                String ns = normalizeStatus(input);
                if (ns == null) return Output.say("Invalid academic status. Enter STUDENT/TEACHER/OTHER:");
                newAcademic = ns;
                return Output.act(Action.APPLY_UPDATE);

            case DONE:
                return Output.act(Action.CLOSE);
        }

        return Output.say("Protocol error.");
    }

    public Output onLoginResult(boolean ok) {
        if (!ok) {
            state = State.DONE;
            return Output.sayAndAct("Wrong username or password. Goodbye.", Action.CLOSE);
        }
        state = State.ASK_UPDATE_YN;
        return Output.say("Welcome back! Do you want to update your details? (yes/no)");
    }

    public Output onRegisterResult(boolean ok) {
        state = State.DONE;
        if (!ok) return Output.sayAndAct("Username already exists. Goodbye.", Action.CLOSE);
        return Output.sayAndAct("Registration complete. Goodbye.", Action.CLOSE);
    }

    public Output onUpdateApplied() {
        state = State.DONE;
        return Output.sayAndAct("Update complete. Goodbye.", Action.CLOSE);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getAcademic() { return academic; }
    public int getYears() { return years; }

    public String getNewPassword() { return newPassword; }
    public Integer getNewYears() { return newYears; }
    public String getNewAcademic() { return newAcademic; }

    private boolean isYes(String s) { return s != null && s.equalsIgnoreCase("yes"); }
    private boolean isNo(String s) { return s != null && s.equalsIgnoreCase("no"); }
    private boolean isEmpty(String s) { return s == null || s.isEmpty(); }

    private boolean isStrongPassword(String p) {
        if (p == null || p.length() < 9) return false;
        boolean upper = false, lower = false, digit = false;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (Character.isUpperCase(c)) upper = true;
            else if (Character.isLowerCase(c)) lower = true;
            else if (Character.isDigit(c)) digit = true;
        }
        return upper && lower && digit;
    }

    private Integer parseYears(String s) {
        try {
            int y = Integer.parseInt(s);
            return (y >= 0 && y <= 90) ? y : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String normalizeStatus(String s) {
        if (s == null) return null;
        String up = s.trim().toUpperCase();
        if (up.equals("STUDENT") || up.equals("TEACHER") || up.equals("OTHER")) return up;
        return null;
    }
}
