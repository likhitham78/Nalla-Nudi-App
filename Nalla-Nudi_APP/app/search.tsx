import React, { useState, useMemo, useEffect, useRef } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  FlatList,
  TouchableOpacity,
  Keyboard,
} from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import { ArrowLeft, Search, X, SlidersHorizontal } from 'lucide-react-native';
import { words, Subject } from '@/data/words';
import { Colors, getSubjectColor, getSubjectLight } from '@/constants/Colors';
import WordCard from '@/components/WordCard';

const SUBJECTS: Subject[] = ['Science', 'Mathematics', 'Commerce'];

export default function SearchScreen() {
  const router = useRouter();
  const params = useLocalSearchParams<{ q?: string; subject?: string }>();
  const inputRef = useRef<TextInput>(null);

  const [query, setQuery] = useState(params.q ?? '');
  const [activeSubject, setActiveSubject] = useState<Subject | null>(
    (params.subject as Subject) || null
  );

  useEffect(() => {
    setTimeout(() => inputRef.current?.focus(), 150);
  }, []);

  const results = useMemo(() => {
    const q = query.toLowerCase().trim();
    return words.filter((w) => {
      const matchesQuery = !q || w.english.toLowerCase().includes(q) || w.kannada.includes(q);
      const matchesSubject = !activeSubject || w.subject === activeSubject;
      return matchesQuery && matchesSubject;
    });
  }, [query, activeSubject]);

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      {/* Search header */}
      <View style={styles.searchHeader}>
        <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
          <ArrowLeft color={Colors.text} size={22} />
        </TouchableOpacity>
        <View style={styles.searchBox}>
          <Search color={Colors.textMuted} size={18} />
          <TextInput
            ref={inputRef}
            style={styles.input}
            value={query}
            onChangeText={setQuery}
            placeholder="Search English words..."
            placeholderTextColor={Colors.textMuted}
            autoCapitalize="none"
            returnKeyType="search"
          />
          {query.length > 0 && (
            <TouchableOpacity onPress={() => setQuery('')} hitSlop={8}>
              <X color={Colors.textMuted} size={16} />
            </TouchableOpacity>
          )}
        </View>
      </View>

      {/* Subject chips */}
      <View style={styles.chipsRow}>
        <View style={styles.filterIcon}>
          <SlidersHorizontal color={Colors.textMuted} size={14} />
        </View>
        {SUBJECTS.map((subject) => {
          const active = activeSubject === subject;
          return (
            <TouchableOpacity
              key={subject}
              style={[
                styles.chip,
                active
                  ? { backgroundColor: getSubjectColor(subject) }
                  : { backgroundColor: getSubjectLight(subject) },
              ]}
              onPress={() => setActiveSubject(active ? null : subject)}
              activeOpacity={0.8}
            >
              <Text style={[styles.chipText, { color: active ? '#fff' : getSubjectColor(subject) }]}>
                {subject}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>

      {/* Results count */}
      <View style={styles.countRow}>
        <Text style={styles.countText}>
          {results.length} {results.length === 1 ? 'result' : 'results'}
        </Text>
        {(query || activeSubject) && (
          <TouchableOpacity
            onPress={() => { setQuery(''); setActiveSubject(null); }}
            style={styles.clearBtn}
          >
            <Text style={styles.clearBtnText}>Clear filters</Text>
          </TouchableOpacity>
        )}
      </View>

      {/* List */}
      <FlatList
        data={results}
        keyExtractor={(item) => item.id}
        renderItem={({ item, index }) => (
          <WordCard word={item} index={index} onPress={() => router.push(`/word/${item.id}`)} />
        )}
        contentContainerStyle={styles.listContent}
        keyboardShouldPersistTaps="handled"
        ListEmptyComponent={
          <View style={styles.emptyState}>
            <View style={styles.emptyIconWrap}>
              <Search color={Colors.textMuted} size={36} />
            </View>
            <Text style={styles.emptyTitle}>No results found</Text>
            <Text style={styles.emptySubtitle}>
              Try a different keyword or remove filters
            </Text>
          </View>
        }
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: Colors.background },

  searchHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    gap: 12,
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
  searchBox: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.surface,
    borderRadius: 14,
    paddingHorizontal: 14,
    paddingVertical: 10,
    gap: 10,
    borderWidth: 1.5,
    borderColor: Colors.border,
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 1,
    shadowRadius: 6,
    elevation: 2,
  },
  input: {
    flex: 1,
    fontFamily: 'Nunito-Regular',
    fontSize: 15,
    color: Colors.text,
    padding: 0,
  },

  chipsRow: {
    flexDirection: 'row',
    gap: 8,
    paddingHorizontal: 20,
    marginBottom: 12,
    alignItems: 'center',
  },
  filterIcon: {
    width: 30,
    height: 30,
    borderRadius: 10,
    backgroundColor: Colors.surfaceAlt,
    alignItems: 'center',
    justifyContent: 'center',
  },
  chip: {
    paddingHorizontal: 14,
    paddingVertical: 7,
    borderRadius: 20,
    shadowColor: Colors.cardShadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.5,
    shadowRadius: 2,
    elevation: 1,
  },
  chipText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 12,
  },

  countRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    marginBottom: 8,
  },
  countText: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 13,
    color: Colors.textMuted,
  },
  clearBtn: {
    paddingHorizontal: 12,
    paddingVertical: 4,
    borderRadius: 8,
    backgroundColor: Colors.primaryLight,
  },
  clearBtnText: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 12,
    color: Colors.primary,
  },

  listContent: {
    paddingHorizontal: 20,
    paddingBottom: 32,
  },

  emptyState: {
    alignItems: 'center',
    marginTop: 64,
    gap: 14,
  },
  emptyIconWrap: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: Colors.surfaceAlt,
    alignItems: 'center',
    justifyContent: 'center',
  },
  emptyTitle: {
    fontFamily: 'Nunito-Bold',
    fontSize: 20,
    color: Colors.text,
  },
  emptySubtitle: {
    fontFamily: 'Nunito-Regular',
    fontSize: 15,
    color: Colors.textMuted,
    textAlign: 'center',
    lineHeight: 24,
  },
});
