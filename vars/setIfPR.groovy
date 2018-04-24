import io.cilium.PRMeta
/*
    SetIfPR is a helper function that returns the first argument if the
    function is running on a PR or it returns the second argument if it's not a
    PR.
*/
def call(String envValueifPR, String envValueIfNotPR){
    pr = new PRMeta(this)
    if (pr.IsPR()){
        return envValueifPR
    }
    return envValueIfNotPR
}
