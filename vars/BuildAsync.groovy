/*
    Build the given job in Async mode with all the parameters from the upstream job.
*/
def call(String job){
    def jobparams = this.params.collect{
        string(name: it.key, value: it.value)
    }
    build(job: job, parameters: jobparams, wait: false)
}
