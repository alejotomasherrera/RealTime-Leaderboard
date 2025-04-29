"use client"

import type React from "react"

import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Gamepad2, Send } from "lucide-react"

interface ScoreSubmissionFormProps {
  onSubmit: (game: string, score: number) => void
}

export default function ScoreSubmissionForm({ onSubmit }: ScoreSubmissionFormProps) {
  const [game, setGame] = useState("")
  const [score, setScore] = useState("")
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!game || !score || isNaN(Number(score))) {
      return
    }

    setIsSubmitting(true)

    try {
      // In a real app, you would send this to your backend
      // await fetch('/api/scores', {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify({ game, score: Number(score) })
      // })

      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1000))

      onSubmit(game, Number(score))
      setGame("")
      setScore("")
    } catch (error) {
      console.error("Error submitting score:", error)
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <Card className="bg-slate-800 border-slate-700">
      <CardHeader>
        <CardTitle>Submit Your Score</CardTitle>
        <CardDescription>Record your latest achievement</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="game">Game</Label>
            <Select value={game} onValueChange={setGame}>
              <SelectTrigger id="game">
                <SelectValue placeholder="Select a game" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="arcade_adventure">Arcade Adventure</SelectItem>
                <SelectItem value="space_shooter">Space Shooter</SelectItem>
                <SelectItem value="puzzle_master">Puzzle Master</SelectItem>
                <SelectItem value="racing_legends">Racing Legends</SelectItem>
                <SelectItem value="strategy_commander">Strategy Commander</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="score">Score</Label>
            <Input
              id="score"
              type="number"
              placeholder="Enter your score"
              value={score}
              onChange={(e) => setScore(e.target.value)}
              min="0"
            />
          </div>

          <div className="flex justify-between items-center pt-2">
            <div className="flex items-center text-sm text-slate-400">
              <Gamepad2 className="mr-2 h-4 w-4" />
              Scores are verified before appearing on the leaderboard
            </div>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? (
                "Submitting..."
              ) : (
                <>
                  Submit <Send className="ml-2 h-4 w-4" />
                </>
              )}
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  )
}
