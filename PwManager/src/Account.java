import java.util.Objects;
public record Account(String platform, String userName, String password, String email) {

    // Override the 'equals' method to compare two 'Account' objects for equality based on platform and username.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return platform.equals(account.platform) && userName.equals(account.userName);
    }

    // Override the 'hashCode' method to generate a hash code based on platform and username.
    @Override
    public int hashCode() {
        return Objects.hash(platform, userName);
    }

    // A method that returns the account data as an array of strings (platform, username, password).
    public String[] getDataArray() {
        return new String[]{platform, userName, password, email};
    }
}
