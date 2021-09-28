import { Component, OnInit } from '@angular/core';
@Component({ templateUrl: 'githubdetails.component.html' })
export class GithubdetailsComponent implements OnInit {    
    healthDetmap:any;        
    constructor() {
    } 
    ngOnInit() {
        console.log("Inside onInit() githubdetails---->");        
         // Go through each key of the indexed object:         
         this.healthDetmap=new Map<String, Map<String,String>>();         
          console.log("values set from success page");
          var healthdetails=localStorage.getItem('healthdet');
          console.log("response values are ",healthdetails);
          console.log(typeof(healthdetails));                                     
          var mapnew = JSON.parse(healthdetails);
          console.log("mapnew");
          console.log(typeof(mapnew));
          console.log(mapnew);
          this.healthDetmap=mapnew;
          console.log("healthDetmap");
          console.log(this.healthDetmap);         
  }                  
}