package com.example.fullhealthcareapplication.ui.components

val titles = listOf(
    "START OF YOUR JOURNEY",
    "What is your age?",
    "What is your gender?",
    "What is your height?",
    "What is your weight?"
)

class OnboardingItems (
    val title: String
){
    companion object{
        fun getData(): List<OnboardingItems>{
            return listOf(
                OnboardingItems(titles[0]),
                OnboardingItems(titles[1]),
                OnboardingItems(titles[2]),
                OnboardingItems(titles[3]),
                OnboardingItems(titles[4])
            )
        }
    }
}