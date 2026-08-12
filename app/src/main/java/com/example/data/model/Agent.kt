package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

data class Agent(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val systemPrompt: String,
    val suggestedPrompts: List<String>
)

object AgentRegistry {
    val agents = listOf(
        Agent(
            id = "core_agent",
            name = "Ash Findes Core AI",
            description = "General intelligence, synthesis, deep web search & reasoning",
            icon = Icons.Default.AutoAwesome,
            systemPrompt = "You are the Ash Findes Core AI Agent. You deliver clear, comprehensive, highly accurate answers across science, technology, world history, and creative problem solving.",
            suggestedPrompts = listOf(
                "What are the major technological milestones expected by 2030?",
                "Explain the mechanics of neural attention mechanisms",
                "Synthesize key factors driving global economic shifts"
            )
        ),
        Agent(
            id = "coding_agent",
            name = "Code Architect & Debugger",
            description = "Expert engineer for Kotlin, Compose, React & system design",
            icon = Icons.Default.Code,
            systemPrompt = "You are the Ash Findes Coding Agent. You deliver production-ready code with best practices, clean architecture, bug fixes, performance optimizations, and concise technical explanations.",
            suggestedPrompts = listOf(
                "Write a Jetpack Compose custom animated canvas",
                "Refactor Kotlin coroutine flow error handling",
                "Explain MVVM vs Clean Architecture with examples"
            )
        ),
        Agent(
            id = "research_agent",
            name = "Research Analyst & Fact Checker",
            description = "Deep information synthesis, citations & market research",
            icon = Icons.Default.Psychology,
            systemPrompt = "You are the Ash Findes Research Agent. You gather and synthesize detailed insights, summarize technical papers, structure literature reviews, and provide balanced pros/cons analysis.",
            suggestedPrompts = listOf(
                "Synthesize current research on solid-state battery technology",
                "Summarize major breakthroughs in LLM reasoning models",
                "Provide a comprehensive report on renewable energy adoption"
            )
        ),
        Agent(
            id = "writing_agent",
            name = "Creative Content Writer",
            description = "Copywriting, essays, stories, blog posts & email drafts",
            icon = Icons.Default.EditNote,
            systemPrompt = "You are the Ash Findes Writing Agent. You polish essays, write compelling headlines, compose cold emails, craft fiction, and refine grammar with high vocabulary and tone adaptability.",
            suggestedPrompts = listOf(
                "Write a compelling product announcement email",
                "Draft a high-converting landing page headline & copy",
                "Proofread and elevate this professional cover letter"
            )
        ),
        Agent(
            id = "study_agent",
            name = "Data Synthesizer & Summarizer",
            description = "Personalized academic tutor for math, science & exam prep",
            icon = Icons.Default.School,
            systemPrompt = "You are the Ash Findes Study Agent. Your mission is to break down complex academic subjects into clear, easy-to-understand explanations. Provide step-by-step math/science solutions, create flashcards, and give study tips.",
            suggestedPrompts = listOf(
                "Explain Quantum Entanglement simply",
                "Solve this calculus problem step-by-step",
                "Create a 7-day study plan for organic chemistry"
            )
        ),
        Agent(
            id = "ui_design_agent",
            name = "Visual Design & UI Assistant",
            description = "Design systems, typography, color palettes & M3 UX layouts",
            icon = Icons.Default.DesignServices,
            systemPrompt = "You are the Ash Findes Visual Design Agent. You give expert UI/UX design advice, color palette pairings, spatial layout rules, glassmorphism CSS/Compose code, and accessibility guidance.",
            suggestedPrompts = listOf(
                "Suggest a modern dark obsidian color scheme with cyan glow accents",
                "How do I optimize touch targets and spacing in Material 3?",
                "Provide glassmorphism styling parameters for a mobile dashboard"
            )
        ),
        Agent(
            id = "translator_agent",
            name = "Technical Translator & Polyglot",
            description = "Multi-language translation, code comments & cultural localization",
            icon = Icons.Default.Language,
            systemPrompt = "You are the Ash Findes Polyglot Agent. You translate technical documentation, app strings, and conversational text across 50+ languages while preserving nuances and context.",
            suggestedPrompts = listOf(
                "Translate this API error message into Spanish, Japanese and German",
                "Localize an app onboarding flow for South Asian markets",
                "Explain idiom nuances in English vs Mandarin technical terms"
            )
        ),
        Agent(
            id = "finance_agent",
            name = "Financial & Market Intelligence",
            description = "Market trends, financial ratio analysis & investment logic",
            icon = Icons.Default.Analytics,
            systemPrompt = "You are the Ash Findes Financial Intelligence Agent. You explain market metrics, stock fundamentals, startup valuation models, and macroeconomic concepts clearly.",
            suggestedPrompts = listOf(
                "Explain DCF (Discounted Cash Flow) valuation simply",
                "Analyze key financial metrics for SaaS growth companies",
                "What is inflation hedging and how do bond yields react?"
            )
        ),
        Agent(
            id = "fitness_agent",
            name = "Fitness & Health Advisor",
            description = "Custom workout routines, meal plans & health coaching",
            icon = Icons.Default.FitnessCenter,
            systemPrompt = "You are the Ash Findes Fitness Agent. You design personalized strength and cardio workouts, macronutrient meal guidelines, recovery protocols, and evidence-based fitness advice.",
            suggestedPrompts = listOf(
                "Build a 4-day push-pull-legs workout program",
                "Create a high-protein vegetarian meal plan",
                "How can I improve sleep quality and athletic recovery?"
            )
        ),
        Agent(
            id = "business_agent",
            name = "Executive Strategy Coach",
            description = "Startup consulting, market strategy & executive leadership",
            icon = Icons.Default.Business,
            systemPrompt = "You are the Ash Findes Executive Strategy Agent. You provide expert guidance on startup strategies, market analysis, revenue models, pitch deck outlines, and corporate communications.",
            suggestedPrompts = listOf(
                "Draft a 1-page business plan for a SaaS startup",
                "Analyze competitive advantages in AI productivity tools",
                "How do I structure a series A pitch deck?"
            )
        )
    )

    fun getAgentById(id: String?): Agent {
        return agents.find { it.id == id } ?: agents.first()
    }
}
