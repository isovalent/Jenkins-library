import io.cilium.PRMeta

/*
   It checks if it is a PR, in case of a PR it'll trigger the given job in case
   of the label is present.
*/
def call(String label, String job){

    pr = new PRMeta(this)
    if (!pr.IsPR()){
        this.echo("BuildIfLabel: is not a PR")
        return
    }

    def labels = pr.getLabels()
    if ( ! labels.contains(label) ){
        this.echo("Label '${label}' is not part of the PR '${labels}'")
        return
    }

    def jobparams = this.params.collect{
        string(name: it.key, value: it.value)
    }
    build(job: job, parameters: jobparams, wait: false)
}
