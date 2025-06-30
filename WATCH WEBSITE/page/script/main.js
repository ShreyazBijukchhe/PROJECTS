// *************** showing and hiding the signup form ********
// getting the signup button 
let signUp = document.getElementById("signUp");

// getting the signup form
let signUpForm = document.getElementsByClassName("signUpForm");

// cehcking for click event int the signup button
// if clicked hidden class is removed to show the singup form
signUp.addEventListener('click',(e)=>{
    Array.from(signUpForm).forEach((item) => {
        item.classList.remove("hidden");
    })
})

// cancle button on the signup button
let signUpCancle = document.getElementById("signUpCancle");

// if cancle button is clicked hidden class is added to the 
// form to hide it
signUpCancle.addEventListener('click',(e)=>{
    e.preventDefault()
    Array.from(signUpForm).forEach((item)=>{
        item.classList.add("hidden");
    })
})

