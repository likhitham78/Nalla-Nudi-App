import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Pressable,
  Platform,
  Share,
} from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import {
  ArrowLeft,
  Heart,
  Volume2,
  BookOpen,
  MessageSquareText,
  Share2,
  ChevronRight,
} from 'lucide-react-native';
import { words } from '@/data/words';
import { Colors, getSubjectColor, getSubjectLight, getSubjectGradient, asGradient } from '@/constants/Colors';
import { useWords } from '@/context/WordContext';
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withSpring,
  withSequence,
  FadeInDown,
} from 'react-native-reanimated';

export default function WordDetailScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { isSaved, saveWord, unsaveWord } = useWords();

  const heartScale = useSharedValue(1);
  const heartStyle = useAnimatedStyle(() => ({
    transform: [{ scale: heartScale.value }],
  }));

  const word = words.find((w) => w.id === id);

  if (!word) {
    return (
      <SafeAreaView style={styles.safe}>
        <Text style={styles.notFound}>Word not found.</Text>
      </SafeAreaView>
    );
  }

  const saved = isSaved(word.id);
  const subjectColor = getSubjectColor(word.subject);
  const subjectLight = getSubjectLight(word.subject);
  const subjectGradient = getSubjectGradient(word.subject);

  const handleSave = () => {
    heartScale.value = withSequence(withSpring(1.4), withSpring(1));
    saved ? unsaveWord(word.id) : saveWord(word);
  };

  const handleSpeak = () => {
    if (Platform.OS === 'web' && typeof window !== 'undefined' && 'speechSynthesis' in window) {
      const utterance = new SpeechSynthesisUtterance(word.english);
      utterance.lang = 'en-US';
      utterance.rate = 0.85;
      window.speechSynthesis.speak(utterance);
    }
  };

  const handleShare = async () => {
    try {
      await Share.share({
        message: `${word.english} - ${word.kannada}\n\n${word.explanation}\n\nExample: ${word.example}\n\nShared from Nalla-Nudi`,
      });
    } catch {}
  };

  return (
    <View style={styles.container}>
      {/* Hero gradient header */}
      <LinearGradient
        colors={subjectGradient as any}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.heroGradient}
      >
        <SafeAreaView edges={['top']} style={{ backgroundColor: 'transparent' }}>
          <View style={styles.heroHeader}>
            <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
              <ArrowLeft color="#fff" size={22} />
            </TouchableOpacity>
            <View style={styles.heroActions}>
              <Animated.View style={heartStyle}>
                <TouchableOpacity onPress={handleSave} style={styles.iconBtn}>
                  <Heart
                    size={20}
                    color={saved ? '#fff' : 'rgba(255,255,255,0.7)'}
                    fill={saved ? '#fff' : 'none'}
                  />
                </TouchableOpacity>
              </Animated.View>
              <TouchableOpacity onPress={handleShare} style={styles.iconBtn}>
                <Share2 size={18} color="rgba(255,255,255,0.7)" />
              </TouchableOpacity>
            </View>
          </View>

          <View style={styles.heroContent}>
            <View style={styles.heroBadge}>
              <Text style={styles.heroBadgeText}>{word.subject}</Text>
            </View>
            <Text style={styles.heroEnglish}>{word.english}</Text>
            <Text style={styles.heroKannada}>{word.kannada}</Text>
          </View>
        </SafeAreaView>
      </LinearGradient>

      {/* Pronounce button overlapping */}
      <View style={styles.pronounceWrapper}>
        <TouchableOpacity
          style={styles.pronounceBtn}
          onPress={handleSpeak}
          activeOpacity={0.8}
        >
          <LinearGradient
            colors={subjectGradient as any}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={styles.pronounceGradient}
          >
            <Volume2 color="#fff" size={18} />
            <Text style={styles.pronounceBtnText}>Pronounce</Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>

      <ScrollView
        style={styles.scroll}
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {/* Explanation */}
        <Animated.View entering={FadeInDown.delay(100)}>
          <View style={styles.section}>
            <View style={styles.sectionHeader}>
              <View style={[styles.sectionIcon, { backgroundColor: Colors.primaryLight }]}>
                <BookOpen color={Colors.primary} size={16} />
              </View>
              <Text style={styles.sectionTitle}>ವಿವರಣೆ (Explanation)</Text>
            </View>
            <View style={styles.sectionCard}>
              <Text style={styles.explanationText}>{word.explanation}</Text>
            </View>
          </View>
        </Animated.View>

        {/* Example */}
        <Animated.View entering={FadeInDown.delay(200)}>
          <View style={styles.section}>
            <View style={styles.sectionHeader}>
              <View style={[styles.sectionIcon, { backgroundColor: Colors.accentLight }]}>
                <MessageSquareText color={Colors.accent} size={16} />
              </View>
              <Text style={styles.sectionTitle}>ಉದಾಹರಣೆ (Example)</Text>
            </View>
            <View style={[styles.sectionCard, styles.exampleCard]}>
              <View style={styles.exampleAccent} />
              <View style={styles.exampleContent}>
                <Text style={styles.exampleQuote}>"</Text>
                <Text style={styles.exampleText}>{word.example}</Text>
              </View>
            </View>
          </View>
        </Animated.View>

        {/* Save CTA */}
        <Animated.View entering={FadeInDown.delay(300)}>
          <TouchableOpacity
            style={[styles.saveCta, saved ? styles.saveCtaSaved : null]}
            onPress={handleSave}
            activeOpacity={0.85}
          >
            {saved ? (
              <View style={styles.saveCtaInnerSaved}>
                <Heart size={20} color={Colors.error} fill={Colors.error} />
                <Text style={styles.saveCtaTextSaved}>Saved to My List</Text>
                <ChevronRight color={Colors.error} size={18} />
              </View>
            ) : (
              <LinearGradient
                colors={subjectGradient as any}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.saveCtaGradient}
              >
                <Heart size={20} color="#fff" />
                <Text style={styles.saveCtaText}>Save to My List</Text>
              </LinearGradient>
            )}
          </TouchableOpacity>
        </Animated.View>

        <View style={{ height: 40 }} />
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: Colors.background },
  container: { flex: 1, backgroundColor: Colors.background },

  heroGradient: {
    borderBottomLeftRadius: 32,
    borderBottomRightRadius: 32,
    overflow: 'hidden',
  },
  heroHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 8,
  },
  backBtn: {
    width: 42,
    height: 42,
    borderRadius: 14,
    backgroundColor: 'rgba(255,255,255,0.2)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  heroActions: {
    flexDirection: 'row',
    gap: 8,
  },
  iconBtn: {
    width: 42,
    height: 42,
    borderRadius: 14,
    backgroundColor: 'rgba(255,255,255,0.2)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  heroContent: {
    paddingHorizontal: 24,
    paddingBottom: 32,
    paddingTop: 8,
  },
  heroBadge: {
    alignSelf: 'flex-start',
    paddingHorizontal: 12,
    paddingVertical: 5,
    borderRadius: 10,
    backgroundColor: 'rgba(255,255,255,0.25)',
    marginBottom: 14,
  },
  heroBadgeText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 12,
    color: '#fff',
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  heroEnglish: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 40,
    color: '#fff',
    lineHeight: 48,
    marginBottom: 6,
  },
  heroKannada: {
    fontFamily: 'Nunito-Bold',
    fontSize: 22,
    color: 'rgba(255,255,255,0.9)',
  },

  pronounceWrapper: {
    alignItems: 'center',
    marginTop: -24,
    zIndex: 10,
  },
  pronounceBtn: {
    borderRadius: 16,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 12,
    elevation: 6,
  },
  pronounceGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    paddingHorizontal: 24,
    paddingVertical: 12,
  },
  pronounceBtnText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 14,
    color: '#fff',
  },

  scroll: { flex: 1, marginTop: 12 },
  scrollContent: { paddingHorizontal: 20 },

  section: { marginBottom: 20 },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    marginBottom: 12,
  },
  sectionIcon: {
    width: 32,
    height: 32,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  sectionTitle: {
    fontFamily: 'Nunito-Bold',
    fontSize: 15,
    color: Colors.text,
  },
  sectionCard: {
    backgroundColor: Colors.surface,
    borderRadius: 16,
    padding: 18,
    borderWidth: 1,
    borderColor: Colors.border,
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 1,
    shadowRadius: 6,
    elevation: 2,
  },
  explanationText: {
    fontFamily: 'Nunito-Regular',
    fontSize: 16,
    color: Colors.text,
    lineHeight: 26,
  },

  exampleCard: {
    flexDirection: 'row',
    overflow: 'hidden',
    padding: 0,
    borderColor: Colors.accentLight,
  },
  exampleAccent: {
    width: 4,
    backgroundColor: Colors.accent,
    borderTopLeftRadius: 16,
    borderBottomLeftRadius: 16,
  },
  exampleContent: {
    flex: 1,
    padding: 18,
    position: 'relative',
  },
  exampleQuote: {
    position: 'absolute',
    top: 4,
    left: 14,
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 48,
    color: Colors.accent,
    opacity: 0.2,
    lineHeight: 48,
  },
  exampleText: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 16,
    color: Colors.text,
    lineHeight: 26,
    paddingTop: 10,
  },

  saveCta: {
    borderRadius: 16,
    overflow: 'hidden',
    marginTop: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 4,
  },
  saveCtaGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 10,
    paddingVertical: 16,
  },
  saveCtaText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 16,
    color: '#fff',
  },
  saveCtaSaved: {
    backgroundColor: Colors.surface,
    borderWidth: 2,
    borderColor: Colors.error,
  },
  saveCtaInnerSaved: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 10,
    paddingVertical: 16,
  },
  saveCtaTextSaved: {
    fontFamily: 'Nunito-Bold',
    fontSize: 16,
    color: Colors.error,
  },

  notFound: {
    fontFamily: 'Nunito-Regular',
    fontSize: 16,
    color: Colors.textMuted,
    textAlign: 'center',
    marginTop: 80,
  },
});
