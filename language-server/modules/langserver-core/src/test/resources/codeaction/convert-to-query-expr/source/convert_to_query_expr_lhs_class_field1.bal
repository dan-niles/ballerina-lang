type User record {|
    string username;
    string firstName;
    string lastName;
    string email;
|};

class Organization {
    User[] users;

    function init(User[] users) {
        self.users = users;
    }
}

public function main() returns error? {
    User[] users = [];

    Organization org = new(users);
    org.users = users;
}
