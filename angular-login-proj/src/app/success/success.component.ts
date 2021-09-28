import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { __param } from 'tslib';

@Component({ templateUrl: 'success.component.html' })
export class SuccessComponent implements OnInit {
    successForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    username="";
    password="";    
    excelsheetname="";    
    private options: string[] = ["Health Insurance", "Underweight Children", "Stunted Children"];
    selectedQuantity = "Health insurance";
  
    constructor
    (
        private http: HttpClient, 
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,        
   )
    {} 

    ngOnInit() {
        this.username=localStorage.getItem('username');        
        this.password=localStorage.getItem('password');
       console.log("getting env values");      
 
this.successForm = this.formBuilder.group({
  excelsheetname: ['', Validators.required],
    
})

// get return url from route parameters or default to '/'
this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
}
  // convenience getter for easy access to form fields
  get f() { return this.successForm.controls; }
  
  getSBDetails()
  {
    const params = new HttpParams().append('excelsheetname', this.excelsheetname);
    console.log("sb details!!!!!!!!!");
    console.log("param value");
    console.log(params);
    return this.http.get('http://localhost:5001/getDetails',{params});
     }
onSubmit() {
  
    console.log('onSubmit() start!!!!!!'); 
     this.submitted = true;        

    // stop here if form is invalid
    if (this.successForm.invalid) {
        return;
    }

    this.loading = true;
    
 this.username=localStorage.getItem('username');        
 this.password=localStorage.getItem('password'); 
 
 this.getSBDetails().subscribe((res:any) => {
   
  console.log("sb service call details");
  console.log(res);   
 let healthDetMap=new Map();
 healthDetMap=res;
 console.log(typeof(res));
 console.log("response object contents")
 for (let key in res) {
  console.log(key, res[key]);
}
localStorage.setItem('healthdet', JSON.stringify(res));

  this.router.navigate(['/githubdetails']);
});
return this.router.navigate(['/success']);
}
}
