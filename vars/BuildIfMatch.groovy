/*
    If in the given str the pattern match, it'll fire the downstream job in async mode.
*/
def call(String str, String pattern, String job){

    if (!(str ==~ pattern)) {
        this.echo("BuildIfMatch: '${str}' does not match with pattern '${pattern}'")
        return
    }
    def jobparams = this.params.collect{
        string(name: it.key, value: it.value)
    }
    build(job: job, parameters: jobparams, wait: false)
}
