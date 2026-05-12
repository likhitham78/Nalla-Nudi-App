import React, { createContext, useContext, useState, useCallback } from 'react';
import { words as allWords, Word } from '@/data/words';

interface WordContextType {
  savedWords: Word[];
  saveWord: (word: Word) => void;
  unsaveWord: (id: string) => void;
  isSaved: (id: string) => boolean;
}

const WordContext = createContext<WordContextType | null>(null);

export function WordProvider({ children }: { children: React.ReactNode }) {
  const [savedWords, setSavedWords] = useState<Word[]>([]);

  const saveWord = useCallback((word: Word) => {
    setSavedWords((prev) => {
      if (prev.find((w) => w.id === word.id)) return prev;
      return [...prev, word];
    });
  }, []);

  const unsaveWord = useCallback((id: string) => {
    setSavedWords((prev) => prev.filter((w) => w.id !== id));
  }, []);

  const isSaved = useCallback(
    (id: string) => savedWords.some((w) => w.id === id),
    [savedWords]
  );

  return (
    <WordContext.Provider value={{ savedWords, saveWord, unsaveWord, isSaved }}>
      {children}
    </WordContext.Provider>
  );
}

export function useWords() {
  const ctx = useContext(WordContext);
  if (!ctx) throw new Error('useWords must be used inside WordProvider');
  return ctx;
}
