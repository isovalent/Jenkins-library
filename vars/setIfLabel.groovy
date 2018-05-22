import io.cilium.PRMeta
/*
    SetIfLabel if the label is present in the PR, it returns the first given
    argument if not it returns the second argument.

    Example:
        stage('Test') {
            environment {
                RUNTIME=SetIfLabel("area/containerd", "CONTAINERD", "DOCKER")
            }
        }
*/
def call(String label, String envValueifLabel, String envValueIfNoLabel){
    pr = new PRMeta(this)
    if ( ! pr.IsPR() ){
        return  envValueIfNoLabel
    }

    def labels = pr.getLabels()
    if ( labels.contains(label) ){
        return envValueifLabel
    }

    return envValueIfNoLabel
}
