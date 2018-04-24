import io.cilium.PRMeta

/*
    Return true if the given job is trigger from a PR.
*/

def call(){
    pr = new PRMeta(this)
    return oo.IsPR()
}
