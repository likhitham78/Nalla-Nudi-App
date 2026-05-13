import React from 'react';
import { View, Text, StyleSheet, Pressable } from 'react-native';
import { Colors, getSubjectColor, getSubjectLight } from '@/constants/Colors';
import { Word } from '@/data/words';
import { Heart } from 'lucide-react-native';
import { useWords } from '@/context/WordContext';
import Animated, { FadeInUp } from 'react-native-reanimated';

interface WordCardProps {
  word: Word;
  onPress: () => void;
  index?: number;
}

export default function WordCard({ word, onPress, index = 0 }: WordCardProps) {
  const { isSaved, saveWord, unsaveWord } = useWords();
  const saved = isSaved(word.id);

  const handleSave = (e: any) => {
    e.stopPropagation?.();
    saved ? unsaveWord(word.id) : saveWord(word);
  };

  return (
    <Animated.View entering={FadeInUp.delay(index * 60).springify()}>
      <Pressable
        style={({ pressed }) => [styles.card, pressed && styles.cardPressed]}
        onPress={onPress}
      >
        <View style={[styles.accentBar, { backgroundColor: getSubjectColor(word.subject) }]} />
        <View style={styles.content}>
          <View style={styles.left}>
            <View style={[styles.badge, { backgroundColor: getSubjectLight(word.subject) }]}>
              <Text style={[styles.badgeText, { color: getSubjectColor(word.subject) }]}>
                {word.subject}
              </Text>
            </View>
            <Text style={styles.english}>{word.english}</Text>
            <Text style={styles.kannada} numberOfLines={1}>{word.kannada}</Text>
          </View>
          <Pressable
            onPress={handleSave}
            hitSlop={12}
            style={({ pressed }) => [styles.saveBtn, pressed && styles.saveBtnPressed]}
          >
            <Heart
              size={20}
              color={saved ? Colors.error : Colors.textMuted}
              fill={saved ? Colors.error : 'none'}
            />
          </Pressable>
        </View>
      </Pressable>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: Colors.surface,
    borderRadius: 16,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: Colors.border,
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 1,
    shadowRadius: 8,
    elevation: 2,
    overflow: 'hidden',
  },
  cardPressed: {
    opacity: 0.9,
    transform: [{ scale: 0.98 }],
  },
  accentBar: {
    height: 3,
    width: '100%',
  },
  content: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 16,
  },
  left: { flex: 1, marginRight: 12 },
  badge: {
    alignSelf: 'flex-start',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    marginBottom: 8,
  },
  badgeText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 10,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  english: {
    fontFamily: 'Nunito-Bold',
    fontSize: 17,
    color: Colors.text,
    marginBottom: 3,
  },
  kannada: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 14,
    color: Colors.primary,
  },
  saveBtn: {
    width: 40,
    height: 40,
    borderRadius: 12,
    backgroundColor: Colors.surfaceAlt,
    alignItems: 'center',
    justifyContent: 'center',
  },
  saveBtnPressed: {
    transform: [{ scale: 0.9 }],
  },
});
