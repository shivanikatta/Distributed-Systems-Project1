import React, { Component } from 'react';
import axios from 'axios';
import Topic from './Topic';
//import PollMsg from './PollMsg';
import SubData from '../config.json';


class Sub1 extends React.Component {
    state = {
        
        name : SubData,
        topicslist : null,
        
        //reload : false
        items: null 
        
    }

    componentDidMount() {

        console.log("In Sub mount");
        var url = "http://localhost:8090/v1/poll/" + this.state.name.sub_id + "?reload=true";
        axios.get(url)
        .then(res => {
        const persons = res.data;
        this.setState({ topicslist : persons });
        console.log("from axios in mount",persons)
        console.log(url)
      })
        
        this.timer = setInterval(() => this.getItems(), 5000);
    }
      
    componentWillUnmount() {
        console.log("In Sub unmount");
        clearInterval(this.timer)
        this.timer = null; 
    }
    copy(a,b)
    {
        console.log("b = ", b );
        a = b;
        console.log("a = ", a )

    }

    async getResponse()
    {
        var url = "http://localhost:8090/v1/poll/" + this.state.name.sub_id ;
        const res = await fetch (url)
        console.log(url)
        const ret = await res.json()
        console.log("Inside Get Response :", ret)
        if(ret.isChange)
        {
            this.setState({ items: ret })
        }
        return ret
    }
      
    getItems() {
       
        // var obj = fetch ('http://localhost:8090/v1/poll/1')
        //   .then(result => result.json())
        //   .then(result => console.log(result))
        //   .then(result => {return result })  
         // .then((obj, result) => this.copy(obj , result) )
         // .then(() => console.log("object",obj))
          //.then(result => this.setState({ items: result }));
        
        //obj = res
        console.log("Finally",obj);
        var obj = this.getResponse()
        
        //const fetchchange = polllist[0];
        //console.log("checking",fetchchange);
    }

    //check (checkvar){

        
    //}
    render() { 
       
        

        const TData = this.state.topicslist;
        console.log("In Sub render");
        console.log("items from polling",this.state.items);
        const PData = this.state.items
        
        if(PData && PData.isChange)
        {
             console.log("Correct hit");
            const PPData = this.state.items.topics;
            console.log("msg for a topic", PPData)
            //this.setState({topicslist : PData})
            //console.log("topiclist after poll" ,this.state.topicslist)
            return (
                <div>
                     <h1> Hii Welcome back {this.state.name.name}</h1>
                    {PPData.map(topic => <Topic key = {topic.topicId} value={topic.topicName} id={topic.topicId} subid = {"1"} issubscribed={topic.subscribed} message={topic.message}/>)}
                                
                </div>
                );
        }


        else if (TData && TData.isChange)
        {
            
            const TTData = this.state.topicslist.topics;
            return (
                <div>
                     <h1> Hii Welcome back {this.state.name.name}</h1>
                    {TTData.map(topic => <Topic key = {topic.topicId} value={topic.topicName} id={topic.topicId} subid = {"1"} ischange={topic.noChange} issubscribed={topic.subscribed} message={topic.message}/>)}
                                
                </div>
                );
        }

        // if(PData && PData.isChange)
        // {
        //      console.log("Correct hit");
        //     const PPData = this.state.items.topics;
        //     return (
        //         <div>
        //              <h1> Hii Welcome back {this.state.name.name}</h1>
        //             {PPData.map(topic => <Topic key = {topic.topicId} value={topic.topicName} id={topic.topicId} subid = {"1"} ischange={topic.noChange} issubscribed={topic.subscribed}/>)}
                                
        //         </div>
        //         );
        // }

        else
        {
            return (
                <div>
                     <h1> Hii Welcome back {this.state.name.name}</h1>             
                </div>
                );
        }

        


    }
}
 

export default Sub1;