import React, { Component } from 'react';
import PubData from './pub.json';
import axios from 'axios';

class publisher extends React.Component {
    state = {
        name : PubData,
        value : "",
        list : null,        
        //value2 : ""
        //topics: ["INR-USD","USD-EUR"]
    }

    componentDidMount() {
        console.log("In Pub mount");
        axios.get(`http://localhost:8070/v1/topics`)
      .then(res => {
        const persons = res.data;
        this.setState({ list : persons });
        console.log("from axios in mount",persons)
      })

        
    }
      
    componentWillUnmount() {
     
    }
      
    handleChange = (event) => {
        console.log("In handle")
        console.log("dropdown value", event.target.value)

        this.setState({ value : event.target.value});

      }

    
    handleSubmit = (event) =>  {

        const finalres = this.state.value
        //alert('New Topic added ' + finalres);
        event.preventDefault();

         var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "http://localhost:8070/v1/advertise",false);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        var input = JSON.stringify({
          "topicName": this.state.value,
          "publisherId": this.state.name.pub_id
        });
        var output;
        output= xhttp.send(input);
        console.log(input);
       
        this.Getdata();
        

      }

      Getdata(){
        console.log("In get data function");
        axios.get(`http://localhost:8070/v1/topics`)
        .then(res => {
          const persons = res.data;
          this.setState({ list : persons });
          console.log("from axios in mount",persons)
        })
        console.log("hello",this.state.list)
      }

    
    render() { 

        const TopicData = this.state.list;
        console.log("In Sub render"); 

        if (TopicData)
        {
            console.log("In if loop")
            const TData = this.state.list.topicNames;
            return (
                <div>
                    <h1> Hello {this.state.name.name}</h1>
                    <p> Topics list</p>
                    <ul>
                        {TData.map(topic => <li key = {topic}> {topic} </li>)}
                    </ul>
                    <h1> Add advertise topic </h1>     
                    <form onSubmit={this.handleSubmit}>
                        <label>
                        Pick new topic:
                        <select value={this.state.value} onChange={this.handleChange}>
                            <option value=" ">Select a topic</option>
                            <option value="INR-USD">INR-USD</option>
                            <option value="INR-EUR">INR-EUR</option>
                            <option value="EUR-INR">EUR-INR</option>
                            <option value="EUR-USD">EUR-USD</option>
                            <option value="USD-INR">USD-INR</option>
                            <option value="USD-EUR">USD-EUR</option>
                            <option value="JPY-INR">JPY-INR</option>
                        </select>
                        </label>
                        <input type="submit" value="Submit" />
                    </form>               
                         
                </div>
            );
        }

        else{
            console.log("In else")
            return (
                <div>
                    <h1> Hello {this.state.name.name}</h1>
                    <h1> Add advertise topic </h1>     
                    <form onSubmit={this.handleSubmit}>
                        <label>
                        Pick new topic:
                        <select value={this.state.value} onChange={this.handleChange}>
                        <option value=" ">Select a topic</option>
                            <option value="INR-USD">INR-USD</option>
                            <option value="INR-EUR">INR-EUR</option>
                            <option value="EUR-INR">EUR-INR</option>
                            <option value="EUR-USD">EUR-USD</option>
                            <option value="USD-INR">USD-INR</option>
                            <option value="USD-EUR">USD-EUR</option>
                            <option value="JPY-INR">JPY-INR</option>
                        </select>
                        </label>
                        <input type="submit" value="Submit" />
                    </form>               
                         
                </div>
            );
        }


       
    }
}
 

export default publisher;