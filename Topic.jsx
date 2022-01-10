import React, { Component } from "react";

class Topic extends Component {

    // constructor(){
    //     super();
    //     this.SubscriptionButton = this.SubscriptionButton.bind(this);
    // }

    state = {
     
        sub_val : this.props.issubscribed,
        topics : this.props.value,
        message : this.props.message
        //value : "Notofication to be printed"

    }

    componentDidMount() {

        //console.log("Hi I am in Mount in Topic");
        // const getDbAnswer = true//fetch a signal from the db
        // if (getDbAnswer === true)
        //   this.setState({ fav: true })
        // else
        //   this.setState({ fav: false })
     }

    render() { 
        //console.log("properties", this.props)
        //console.log("subscribed", this.props.issubscribed);

        //console.log("state var",this.state.sub_val)
        console.log("in topic",this.state.message);
        console.log("from props", this.props)
        console.log("msg from props", this.props.message)

        return (
        <React.Fragment>
                          
            <button onClick={ () => this.SubscriptionButton()} className="btn btn-secondary btn-sm">{this.isSubscribe()} </button> <span>{this.state.topics}</span> <textarea style={this.state.sub_val === "true" ? {} : { display: 'none' }} name="body"  value={this.props.message}/>
            <p> </p>

           
        </React.Fragment>
        );

    }

    isSubscribe(){
        //console.log("comparison",this.state.sub_val);
        if(this.state.sub_val === "true"){
            //console.log("helo true")
            return "UnSubscribe to ";

        }
        else{
            return "Subscribe to ";
        }
      
    }

    SubscriptionButton = topic => {

        // var xhttp = new XMLHttpRequest();
        // xhttp.open("POST", "http://localhost:8090/v1/subscribe",false);
        // xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        
        

        var subunsub = this.state.sub_val === "true" ? "false" : "true";
        this.setState({ sub_val: subunsub})
        var path;
        if(subunsub === "true")
        {
            path = "subscribe";
        }
        else
        {
            path = "unsubscribe"
        }

        
        
            //this.setState({ count: 1})
            console.log("Inside sub")
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "http://localhost:8090/v1/" + path,false);
            xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            var input = JSON.stringify({
                "subscriberId": this.props.subid,
                //"type": "subscribed",
                "topicName": this.state.topics
              });
            //this.setState({ count: 1console.log("you can do")
        

        var output;
        output= xhttp.send(input);
        console.log(input);
        //console.log(topic)
        //console.log(this.state.count);
        //console.log(input);


    }
}
 
export default Topic;