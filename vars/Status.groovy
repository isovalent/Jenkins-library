import io.cilium.PRMeta
import org.kohsuke.github.GHCommitState

def call(String state, String context){
    pr = new PRMeta(this)
    if (!pr.IsPR()){
        this.echo("GHStatus: is not a PR")
        return
    }

    def repo = pr.getRepo()
    if ( !repo ){
        this.echo("GHStatus: cannot get Repo")
        return
    }

    def GHState
    switch (state) {
        case "FAILURE":
            GHState = GHCommitState.FAILURE
            break
        case "SUCCESS":
            GHState = GHCommitState.SUCCESS
            break
        case "PENDING":
            GHState = GHCommitState.PENDING
            break
    }

    try{
        def sha1 = this.env.getProperty("sha1")
        def url = this.env.getProperty("BUILD_URL")
        repo.createCommitStatus(sha1, GHState, url, "", "${context}")
    } catch(Exception e) {
        this.echo("GHStatus: Cannot set state '${e}'")
        return false
    }
}
