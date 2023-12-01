type SampleErrorData record {|
    int userCode;
    string userReason;
|};

type SampleError error<SampleErrorData>;

public function test() {
    match bar {
        () => {
        }
        "east" => {
            SampleError err = error("Transaction Failure", error("Database Error"), userCode = 20,
                                        userReason = "deadlock condition");
            fail err;
        }
    } on fail var error(message, cause, userCode = code, userReason = reason) {
        string msg = message;
    }
}
