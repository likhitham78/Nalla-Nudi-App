import React, { useState, useMemo } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Pressable,
  Dimensions,
} from 'react-native';
import { useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import { Search, BookOpen, Brain, Heart, Sparkles, ArrowRight, Flame } from 'lucide-react-native';
import { words, getWordOfTheDay, Subject } from '@/data/words';
import { Colors, getSubjectColor, getSubjectLight, getSubjectGradient, asGradient } from '@/constants/Colors';
import { useWords } from '@/context/WordContext';
import WordCard from '@/components/WordCard';
import Animated, { FadeInDown, FadeInRight } from 'react-native-reanimated';

const { width: SCREEN_WIDTH } = Dimensions.get('window');
const SUBJECTS: Subject[] = ['Science', 'Mathematics', 'Commerce'];

export default function HomeScreen() {
  const router = useRouter();
  const { savedWords } = useWords();
  const [activeSubject, setActiveSubject] = useState<Subject | null>(null);

  const wordOfTheDay = useMemo(() => getWordOfTheDay(), []);

  const handleSearchPress = () => {
    router.push({ pathname: '/search', params: { q: '', subject: activeSubject ?? '' } });
  };

  return (
    <View style={styles.container}>
      <ScrollView
        style={styles.scroll}
        contentContainerStyle={styles.content}
        showsVerticalScrollIndicator={false}
        stickyHeaderIndices={[0]}
      >
        {/* Gradient Header */}
        <View style={styles.headerSection}>
          <LinearGradient
            colors={asGradient(Colors.gradientHero)}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={styles.headerGradient}
          >
            <SafeAreaView edges={['top']} style={{ backgroundColor: 'transparent' }}>
              <View style={styles.headerContent}>
                <View>
                  <Animated.Text entering={FadeInRight.delay(100)} style={styles.appTitle}>
                    ನಲ್ಲ-ನುಡಿ
                  </Animated.Text>
                  <Animated.Text entering={FadeInRight.delay(200)} style={styles.appSubtitle}>
                    Nalla-Nudi · Bridge Dictionary
                  </Animated.Text>
                </View>
                <Animated.View entering={FadeInRight.delay(300)} style={styles.headerBadge}>
                  <BookOpen color="#fff" size={20} />
                </Animated.View>
              </View>
            </SafeAreaView>
          </LinearGradient>

          {/* Search Bar - overlapping the gradient */}
          <View style={styles.searchWrapper}>
            <Pressable onPress={handleSearchPress} style={styles.searchContainer}>
              <View style={styles.searchInner}>
                <Search color={Colors.textMuted} size={20} />
                <Text style={styles.searchPlaceholder}>Search English words...</Text>
              </View>
            </Pressable>
          </View>
        </View>

        {/* Subject Chips */}
        <View style={styles.chipsSection}>
          {SUBJECTS.map((subject, i) => {
            const active = activeSubject === subject;
            return (
              <Animated.View key={subject} entering={FadeInDown.delay(100 + i * 80)}>
                <TouchableOpacity
                  style={[
                    styles.chip,
                    active && { backgroundColor: getSubjectColor(subject) },
                    !active && { backgroundColor: getSubjectLight(subject) },
                  ]}
                  onPress={() => setActiveSubject(active ? null : subject)}
                  activeOpacity={0.8}
                >
                  <Text
                    style={[
                      styles.chipText,
                      { color: active ? '#fff' : getSubjectColor(subject) },
                    ]}
                  >
                    {subject}
                  </Text>
                </TouchableOpacity>
              </Animated.View>
            );
          })}
        </View>

        {/* Word of the Day */}
        <Animated.View entering={FadeInDown.delay(300)}>
          <View style={styles.sectionHeader}>
            <View style={styles.sectionHeaderLeft}>
              <View style={styles.wotdIconWrap}>
                <Flame color={Colors.accent} size={16} />
              </View>
              <Text style={styles.sectionTitle}>Word of the Day</Text>
            </View>
          </View>
          <Pressable
            style={styles.wotdCard}
            onPress={() => router.push(`/word/${wordOfTheDay.id}`)}
          >
            <LinearGradient
              colors={getSubjectGradient(wordOfTheDay.subject) as any}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={styles.wotdGradient}
            >
              <View style={styles.wotdContent}>
                <View style={styles.wotdTopRow}>
                  <View style={styles.wotdBadge}>
                    <Text style={styles.wotdBadgeText}>{wordOfTheDay.subject}</Text>
                  </View>
                  <View style={styles.wotdTapHint}>
                    <Text style={styles.wotdTapText}>Tap to learn</Text>
                    <ArrowRight color="#fff" size={14} />
                  </View>
                </View>
                <Text style={styles.wotdEnglish}>{wordOfTheDay.english}</Text>
                <Text style={styles.wotdKannada}>{wordOfTheDay.kannada}</Text>
                <Text style={styles.wotdPreview} numberOfLines={2}>
                  {wordOfTheDay.explanation}
                </Text>
              </View>
            </LinearGradient>
          </Pressable>
        </Animated.View>

        {/* Quick Access */}
        <Animated.View entering={FadeInDown.delay(400)}>
          <View style={styles.sectionHeader}>
            <Text style={styles.sectionTitle}>Quick Access</Text>
          </View>
          <View style={styles.quickRow}>
            <Pressable
              style={styles.quickCard}
              onPress={() => router.push('/mylist')}
            >
              <LinearGradient
                colors={['#DBEAFE', '#EFF6FF'] as const}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.quickGradient}
              >
                <View style={[styles.quickIconWrap, { backgroundColor: Colors.primary }]}>
                  <Heart color="#fff" size={20} />
                </View>
                <Text style={[styles.quickLabel, { color: Colors.primaryDark }]}>My List</Text>
                <Text style={[styles.quickCount, { color: Colors.primary }]}>
                  {savedWords.length} words
                </Text>
              </LinearGradient>
            </Pressable>
            <Pressable
              style={styles.quickCard}
              onPress={() => router.push('/flashcards')}
            >
              <LinearGradient
                colors={['#D1FAE5', '#ECFDF5'] as const}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.quickGradient}
              >
                <View style={[styles.quickIconWrap, { backgroundColor: Colors.success }]}>
                  <Brain color="#fff" size={20} />
                </View>
                <Text style={[styles.quickLabel, { color: Colors.successDark }]}>Flashcards</Text>
                <Text style={[styles.quickCount, { color: Colors.success }]}>Practice now</Text>
              </LinearGradient>
            </Pressable>
          </View>
        </Animated.View>

        {/* Explore Words */}
        <Animated.View entering={FadeInDown.delay(500)}>
          <View style={styles.sectionHeader}>
            <Text style={styles.sectionTitle}>Explore Words</Text>
            <TouchableOpacity onPress={() => router.push({ pathname: '/search', params: { q: '', subject: '' } })}>
              <View style={styles.seeAllBtn}>
                <Text style={styles.seeAll}>See all</Text>
                <ArrowRight color={Colors.primary} size={14} />
              </View>
            </TouchableOpacity>
          </View>
        </Animated.View>
        {words.slice(0, 5).map((word, i) => (
          <WordCard key={word.id} word={word} index={i} onPress={() => router.push(`/word/${word.id}`)} />
        ))}
        <View style={{ height: 24 }} />
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },

  scroll: { flex: 1 },
  content: { paddingBottom: 32 },

  headerSection: {
    overflow: 'hidden',
    paddingBottom: 28,
  },
  headerGradient: {
    borderBottomLeftRadius: 28,
    borderBottomRightRadius: 28,
    paddingTop: 8,
  },
  headerContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingBottom: 24,
  },
  appTitle: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 30,
    color: '#fff',
    letterSpacing: -0.5,
  },
  appSubtitle: {
    fontFamily: 'Nunito-Regular',
    fontSize: 13,
    color: 'rgba(255,255,255,0.75)',
    marginTop: 3,
  },
  headerBadge: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(255,255,255,0.2)',
    alignItems: 'center',
    justifyContent: 'center',
  },

  searchWrapper: {
    marginTop: -28,
    paddingHorizontal: 20,
  },
  searchContainer: {
    borderRadius: 18,
    backgroundColor: Colors.surface,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 6,
  },
  searchInner: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 18,
    paddingVertical: 16,
    gap: 12,
  },
  searchPlaceholder: {
    fontFamily: 'Nunito-Regular',
    fontSize: 16,
    color: Colors.textMuted,
  },

  chipsSection: {
    flexDirection: 'row',
    gap: 10,
    paddingHorizontal: 20,
    marginTop: 20,
    marginBottom: 24,
  },
  chip: {
    paddingHorizontal: 18,
    paddingVertical: 9,
    borderRadius: 24,
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.5,
    shadowRadius: 4,
    elevation: 2,
  },
  chipText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 13,
  },

  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 14,
    paddingHorizontal: 20,
  },
  sectionHeaderLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  wotdIconWrap: {
    width: 28,
    height: 28,
    borderRadius: 14,
    backgroundColor: Colors.accentLight,
    alignItems: 'center',
    justifyContent: 'center',
  },
  sectionTitle: {
    fontFamily: 'Nunito-Bold',
    fontSize: 18,
    color: Colors.text,
  },
  seeAllBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  seeAll: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 14,
    color: Colors.primary,
  },

  wotdCard: {
    marginHorizontal: 20,
    borderRadius: 20,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.15,
    shadowRadius: 16,
    elevation: 8,
  },
  wotdGradient: {
    borderRadius: 20,
  },
  wotdContent: {
    padding: 22,
  },
  wotdTopRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 14,
  },
  wotdBadge: {
    paddingHorizontal: 12,
    paddingVertical: 5,
    borderRadius: 10,
    backgroundColor: 'rgba(255,255,255,0.25)',
  },
  wotdBadgeText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 11,
    color: '#fff',
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  wotdTapHint: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  wotdTapText: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 12,
    color: 'rgba(255,255,255,0.8)',
  },
  wotdEnglish: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 28,
    color: '#fff',
    marginBottom: 4,
  },
  wotdKannada: {
    fontFamily: 'Nunito-Bold',
    fontSize: 18,
    color: 'rgba(255,255,255,0.9)',
    marginBottom: 10,
  },
  wotdPreview: {
    fontFamily: 'Nunito-Regular',
    fontSize: 14,
    color: 'rgba(255,255,255,0.75)',
    lineHeight: 22,
  },

  quickRow: {
    flexDirection: 'row',
    gap: 12,
    paddingHorizontal: 20,
    marginBottom: 24,
  },
  quickCard: {
    flex: 1,
    borderRadius: 18,
    overflow: 'hidden',
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 1,
    shadowRadius: 8,
    elevation: 3,
  },
  quickGradient: {
    padding: 18,
    alignItems: 'center',
    gap: 10,
  },
  quickIconWrap: {
    width: 44,
    height: 44,
    borderRadius: 14,
    alignItems: 'center',
    justifyContent: 'center',
  },
  quickLabel: {
    fontFamily: 'Nunito-Bold',
    fontSize: 16,
  },
  quickCount: {
    fontFamily: 'Nunito-Regular',
    fontSize: 12,
  },
});
