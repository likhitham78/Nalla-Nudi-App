import React, { useState, useCallback } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Pressable,
  Dimensions,
} from 'react-native';
import { useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import { ArrowLeft, Check, ChevronRight, RotateCcw, Trophy, X, Zap } from 'lucide-react-native';
import { Colors, getSubjectColor, getSubjectLight, getSubjectGradient, asGradient } from '@/constants/Colors';
import { useWords } from '@/context/WordContext';
import { words as allWords } from '@/data/words';
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  withSpring,
  interpolate,
  Extrapolation,
  FadeInDown,
  SlideInRight,
  SlideOutLeft,
} from 'react-native-reanimated';
import { GestureHandlerRootView } from 'react-native-gesture-handler';

const { width: SCREEN_WIDTH } = Dimensions.get('window');
const CARD_WIDTH = SCREEN_WIDTH - 56;
const CARD_HEIGHT = 380;

export default function FlashcardsScreen() {
  const router = useRouter();
  const { savedWords } = useWords();
  const deck = savedWords.length > 0 ? savedWords : allWords.slice(0, 8);

  const [currentIndex, setCurrentIndex] = useState(0);
  const [knownIds, setKnownIds] = useState<Set<string>>(new Set());
  const [finished, setFinished] = useState(false);

  const flipValue = useSharedValue(0);
  const isFlipped = useSharedValue(false);
  const cardScale = useSharedValue(1);

  const frontStyle = useAnimatedStyle(() => {
    const rotate = interpolate(flipValue.value, [0, 1], [0, 180], Extrapolation.CLAMP);
    return {
      transform: [{ rotateY: `${rotate}deg` }, { scale: cardScale.value }],
      backfaceVisibility: 'hidden',
    };
  });

  const backStyle = useAnimatedStyle(() => {
    const rotate = interpolate(flipValue.value, [0, 1], [180, 360], Extrapolation.CLAMP);
    return {
      transform: [{ rotateY: `${rotate}deg` }, { scale: cardScale.value }],
      backfaceVisibility: 'hidden',
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
    };
  });

  const flipCard = () => {
    cardScale.value = withSpring(0.96, { damping: 15 }, () => {
      cardScale.value = withSpring(1, { damping: 12 });
    });
    if (isFlipped.value) {
      flipValue.value = withTiming(0, { duration: 400 });
      isFlipped.value = false;
    } else {
      flipValue.value = withTiming(1, { duration: 400 });
      isFlipped.value = true;
    }
  };

  const goNext = useCallback(() => {
    flipValue.value = withTiming(0, { duration: 250 });
    isFlipped.value = false;
    cardScale.value = withSpring(0.95, { damping: 15 }, () => {
      cardScale.value = withSpring(1, { damping: 12 });
    });
    setTimeout(() => {
      if (currentIndex + 1 >= deck.length) {
        setFinished(true);
      } else {
        setCurrentIndex((i) => i + 1);
      }
    }, 250);
  }, [currentIndex, deck.length]);

  const markKnown = useCallback(() => {
    setKnownIds((prev) => new Set([...prev, deck[currentIndex].id]));
    goNext();
  }, [currentIndex, deck, goNext]);

  const restart = () => {
    setCurrentIndex(0);
    setKnownIds(new Set());
    setFinished(false);
    flipValue.value = 0;
    isFlipped.value = false;
  };

  const current = deck[currentIndex];

  // --- Finished Screen ---
  if (finished) {
    const pct = Math.round((knownIds.size / deck.length) * 100);
    return (
      <SafeAreaView style={styles.safe} edges={['top']}>
        <View style={styles.header}>
          <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
            <ArrowLeft color={Colors.text} size={22} />
          </TouchableOpacity>
          <Text style={styles.headerTitle}>Flashcards</Text>
          <View style={{ width: 42 }} />
        </View>
        <View style={styles.finishContainer}>
          <Animated.View entering={FadeInDown.delay(100)}>
            <View style={styles.trophyCircle}>
              <LinearGradient
                colors={['#FDE68A', '#F59E0B'] as const}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.trophyGradient}
              >
                <Trophy color="#fff" size={44} />
              </LinearGradient>
            </View>
          </Animated.View>
          <Animated.View entering={FadeInDown.delay(200)}>
            <Text style={styles.finishTitle}>Great work!</Text>
            <Text style={styles.finishSub}>
              You reviewed {deck.length} words
            </Text>
          </Animated.View>
          <Animated.View entering={FadeInDown.delay(300)} style={styles.statsRow}>
            <View style={[styles.statBox, { backgroundColor: Colors.successLight }]}>
              <Text style={[styles.statNum, { color: Colors.success }]}>{knownIds.size}</Text>
              <Text style={[styles.statLabel, { color: Colors.success }]}>Known</Text>
            </View>
            <View style={[styles.statBox, { backgroundColor: Colors.primaryLight }]}>
              <Text style={[styles.statNum, { color: Colors.primary }]}>{deck.length - knownIds.size}</Text>
              <Text style={[styles.statLabel, { color: Colors.primary }]}>To Review</Text>
            </View>
            <View style={[styles.statBox, { backgroundColor: Colors.accentLight }]}>
              <Text style={[styles.statNum, { color: Colors.accent }]}>{pct}%</Text>
              <Text style={[styles.statLabel, { color: Colors.accent }]}>Score</Text>
            </View>
          </Animated.View>
          <Animated.View entering={FadeInDown.delay(400)} style={styles.finishActions}>
            <TouchableOpacity style={styles.restartBtn} onPress={restart}>
              <LinearGradient
                colors={asGradient(Colors.gradientHero)}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.restartGradient}
              >
                <RotateCcw color="#fff" size={18} />
                <Text style={styles.restartBtnText}>Study Again</Text>
              </LinearGradient>
            </TouchableOpacity>
            <TouchableOpacity style={styles.doneBtn} onPress={() => router.back()}>
              <Text style={styles.doneBtnText}>Done</Text>
            </TouchableOpacity>
          </Animated.View>
        </View>
      </SafeAreaView>
    );
  }

  const subjectColor = getSubjectColor(current.subject);
  const subjectLight = getSubjectLight(current.subject);
  const subjectGradient = getSubjectGradient(current.subject);

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <SafeAreaView style={styles.safe} edges={['top']}>
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
            <ArrowLeft color={Colors.text} size={22} />
          </TouchableOpacity>
          <Text style={styles.headerTitle}>Flashcards</Text>
          <View style={styles.progressPill}>
            <Zap color={Colors.primary} size={14} />
            <Text style={styles.progress}>
              {currentIndex + 1}/{deck.length}
            </Text>
          </View>
        </View>

        {/* Progress bar */}
        <View style={styles.progressBar}>
          <View style={[styles.progressTrack]}>
            <Animated.View
              style={[
                styles.progressFill,
                {
                  width: `${((currentIndex + 1) / deck.length) * 100}%`,
                },
              ]}
            />
          </View>
        </View>

        {/* Hint */}
        <Text style={styles.hint}>Tap card to reveal meaning</Text>

        {/* Card */}
        <View style={styles.cardContainer}>
          <Pressable onPress={flipCard} style={styles.cardWrapper}>
            {/* Front */}
            <Animated.View style={[styles.card, frontStyle]}>
              <View style={styles.cardInner}>
                <LinearGradient
                  colors={['#F8FAFC', '#EFF6FF'] as const}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 1 }}
                  style={[styles.cardSurface, { borderColor: subjectLight }]}
                >
                  <View style={[styles.subjectBadge, { backgroundColor: subjectLight }]}>
                    <Text style={[styles.subjectText, { color: subjectColor }]}>{current.subject}</Text>
                  </View>
                  <Text style={styles.cardEnglish}>{current.english}</Text>
                  <View style={styles.tapHintRow}>
                    <View style={[styles.tapDot, { backgroundColor: subjectColor }]} />
                    <Text style={[styles.cardHint, { color: subjectColor }]}>Tap to see meaning</Text>
                  </View>
                </LinearGradient>
              </View>
            </Animated.View>

            {/* Back */}
            <Animated.View style={[styles.card, backStyle]}>
              <View style={styles.cardInner}>
                <LinearGradient
                  colors={subjectGradient as any}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 1 }}
                  style={styles.cardSurfaceBack}
                >
                  <View style={[styles.subjectBadge, { backgroundColor: 'rgba(255,255,255,0.25)' }]}>
                    <Text style={[styles.subjectText, { color: '#fff' }]}>{current.subject}</Text>
                  </View>
                  <Text style={styles.cardKannada}>{current.kannada}</Text>
                  <Text style={styles.cardExplanation} numberOfLines={5}>{current.explanation}</Text>
                </LinearGradient>
              </View>
            </Animated.View>
          </Pressable>
        </View>

        {/* Actions */}
        <View style={styles.actions}>
          <TouchableOpacity style={styles.nextBtn} onPress={goNext} activeOpacity={0.85}>
            <ChevronRight color={Colors.textSecondary} size={20} />
            <Text style={styles.nextBtnText}>Skip</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.knownBtn} onPress={markKnown} activeOpacity={0.85}>
            <LinearGradient
              colors={asGradient(Colors.gradientSuccess)}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={styles.knownGradient}
            >
              <Check color="#fff" size={22} />
              <Text style={styles.knownBtnText}>I know this</Text>
            </LinearGradient>
          </TouchableOpacity>
        </View>

        <View style={{ height: 32 }} />
      </SafeAreaView>
    </GestureHandlerRootView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: Colors.background },

  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 12,
  },
  backBtn: {
    width: 42,
    height: 42,
    borderRadius: 14,
    backgroundColor: Colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 1,
    shadowRadius: 4,
    elevation: 2,
  },
  headerTitle: {
    fontFamily: 'Nunito-Bold',
    fontSize: 18,
    color: Colors.text,
  },
  progressPill: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 12,
    backgroundColor: Colors.primaryLight,
  },
  progress: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 13,
    color: Colors.primary,
  },

  progressBar: {
    paddingHorizontal: 20,
    marginBottom: 12,
  },
  progressTrack: {
    height: 6,
    backgroundColor: Colors.border,
    borderRadius: 3,
    overflow: 'hidden',
  },
  progressFill: {
    height: 6,
    backgroundColor: Colors.primary,
    borderRadius: 3,
  },

  hint: {
    fontFamily: 'Nunito-Regular',
    fontSize: 13,
    color: Colors.textMuted,
    textAlign: 'center',
    marginBottom: 20,
  },

  cardContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 28,
  },
  cardWrapper: {
    width: CARD_WIDTH,
    height: CARD_HEIGHT,
  },
  card: {
    width: CARD_WIDTH,
    height: CARD_HEIGHT,
  },
  cardInner: {
    flex: 1,
    borderRadius: 24,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.12,
    shadowRadius: 20,
    elevation: 8,
  },
  cardSurface: {
    flex: 1,
    padding: 28,
    borderWidth: 2,
    borderRadius: 24,
    alignItems: 'flex-start',
    justifyContent: 'center',
  },
  cardSurfaceBack: {
    flex: 1,
    padding: 28,
    borderRadius: 24,
    justifyContent: 'flex-start',
  },
  subjectBadge: {
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderRadius: 10,
    marginBottom: 20,
  },
  subjectText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 11,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  cardEnglish: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 42,
    color: Colors.text,
    lineHeight: 50,
    marginBottom: 20,
  },
  tapHintRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  tapDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
  },
  cardHint: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 14,
  },
  cardKannada: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 30,
    color: '#fff',
    marginBottom: 18,
    lineHeight: 38,
  },
  cardExplanation: {
    fontFamily: 'Nunito-Regular',
    fontSize: 16,
    color: 'rgba(255,255,255,0.9)',
    lineHeight: 26,
  },

  actions: {
    flexDirection: 'row',
    gap: 12,
    paddingHorizontal: 28,
    marginTop: 24,
  },
  nextBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 6,
    paddingVertical: 16,
    borderRadius: 16,
    backgroundColor: Colors.surface,
    borderWidth: 1.5,
    borderColor: Colors.border,
  },
  nextBtnText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 15,
    color: Colors.textSecondary,
  },
  knownBtn: {
    flex: 2,
    borderRadius: 16,
    overflow: 'hidden',
    shadowColor: Colors.success,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 4,
  },
  knownGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    paddingVertical: 16,
  },
  knownBtnText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 15,
    color: '#fff',
  },

  // Finish screen
  finishContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 32,
  },
  trophyCircle: {
    width: 100,
    height: 100,
    borderRadius: 50,
    overflow: 'hidden',
    marginBottom: 24,
    shadowColor: Colors.accent,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 12,
    elevation: 6,
  },
  trophyGradient: {
    width: 100,
    height: 100,
    borderRadius: 50,
    alignItems: 'center',
    justifyContent: 'center',
  },
  finishTitle: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 32,
    color: Colors.text,
    textAlign: 'center',
    marginBottom: 8,
  },
  finishSub: {
    fontFamily: 'Nunito-Regular',
    fontSize: 16,
    color: Colors.textSecondary,
    textAlign: 'center',
    lineHeight: 26,
    marginBottom: 28,
  },
  statsRow: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 32,
  },
  statBox: {
    flex: 1,
    borderRadius: 16,
    padding: 18,
    alignItems: 'center',
  },
  statNum: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 32,
  },
  statLabel: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 12,
    marginTop: 4,
  },
  finishActions: {
    width: '100%',
    alignItems: 'center',
  },
  restartBtn: {
    width: '100%',
    borderRadius: 16,
    overflow: 'hidden',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 4,
    marginBottom: 12,
  },
  restartGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    paddingVertical: 16,
    justifyContent: 'center',
  },
  restartBtnText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 16,
    color: '#fff',
  },
  doneBtn: {
    paddingVertical: 14,
  },
  doneBtnText: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 16,
    color: Colors.textMuted,
  },
});
